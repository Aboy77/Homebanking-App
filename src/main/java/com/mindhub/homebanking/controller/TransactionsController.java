package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TransactionsController {

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @PostMapping("/api/transactions")
    public ResponseEntity<?> transfer(@RequestParam String number, @RequestParam String amount, @RequestParam String current, @RequestParam String description, Authentication authentication) {
        Double amounts = Double.parseDouble(amount);
        Client client = clientRepository.findByEmail(authentication.getName());

        Account account = accountRepository.findByNumber(number);
        Account currentAccount = accountRepository.findByNumber(current);

        String balanceString = account.getBalance();
        String currentBalance = currentAccount.getBalance();

        Double balance = Double.parseDouble(balanceString);
        Double curr = Double.parseDouble(currentBalance);

        if(account.getNumber().length() < 1) {
            return new ResponseEntity<>("Cannot find an account", HttpStatus.FORBIDDEN);
        }

        if(curr < amounts) {
            return new ResponseEntity<>("Insufficient current balance", HttpStatus.FORBIDDEN);
        }

        if(number.isEmpty()) {
            return new ResponseEntity<>("Please select an account",HttpStatus.FORBIDDEN);
        }

        if(amount.isEmpty()) {
            return new ResponseEntity<>("Minimum transfer $1", HttpStatus.FORBIDDEN);
        }

        //increase destiny amount
        Double change = balance + amounts;
        String currtBalance = String.valueOf(change);

        //decreasing current account amount
        Double rest = curr - amounts;
        String restBalance = String.valueOf(rest);

        account.setBalance(currtBalance);
        currentAccount.setBalance(restBalance);

        //creating transactions
        Transaction destiny = new Transaction(TransactionType.Credit, amounts, description, LocalDate.now());
        Transaction origin = new Transaction(TransactionType.Debit, amounts, description, LocalDate.now());

        //saving transactions
        transactionRepository.save(destiny);
        transactionRepository.save(origin);


        //adding transactions to accounts
        account.addTransaction(destiny);
        currentAccount.addTransaction(origin);

        accountRepository.save(account);
        accountRepository.save(currentAccount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/transfer")
    public ResponseEntity<?> transferCash(Authentication authentication, @RequestParam Long id,@RequestParam Double amount, @RequestParam Long cbu) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account destinyAccount = accountRepository.findByCbu(cbu);
        Account ownAccount = accountRepository.findAccountById(id);

        List<Account> accountList = client.getAccounts().stream().filter(account1 -> account1.equals(destinyAccount)).collect(Collectors.toList());

        if(accountList.size() > 0) {
            return new ResponseEntity<>("Cannot send to your own account", HttpStatus.FORBIDDEN);
        }

        if(destinyAccount == null) {
            return new ResponseEntity<>("Cannot find destiny account!", HttpStatus.FORBIDDEN);
        }

        // get own account info
        String ownBalance = ownAccount.getBalance();
        Double ownNumber = Double.parseDouble(ownBalance);

        if(amount > ownNumber) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        Double currentOwn = ownNumber - amount;
        String currentBalanceOwn = String.valueOf(currentOwn);

        ownAccount.setBalance(currentBalanceOwn);

        Transaction transaction1 = new Transaction(TransactionType.Debit, amount, "Send to " + destinyAccount.getClient().getFirstName() + " " + destinyAccount.getClient().getLastName(), LocalDate.now());

        ownAccount.addTransaction(transaction1);

        // get destiny account info
        String destinyBalance = destinyAccount.getBalance();
        Double destinyNumber = Double.parseDouble(destinyBalance);
        Double currentDestiny = destinyNumber + amount;
        String currentBalanceDestiny = String.valueOf(currentDestiny);

        destinyAccount.setBalance(currentBalanceDestiny);

        Transaction transaction2 = new Transaction(TransactionType.Credit, amount, "Received from " + ownAccount.getClient().getFirstName() + " " + ownAccount.getClient().getLastName(), LocalDate.now());

        destinyAccount.addTransaction(transaction2);

        transactionRepository.save(transaction1);
        transactionRepository.save(transaction2);
        accountRepository.save(ownAccount);
        accountRepository.save(destinyAccount);

        return new ResponseEntity<>("Transfer Success", HttpStatus.ACCEPTED);
    }
}
