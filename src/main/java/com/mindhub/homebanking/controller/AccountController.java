package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.CardRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAll() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/api/clients/current/account")
    public ResponseEntity<?> createAccount(Authentication authentication, @RequestParam AccountType accountType) {

        int random = (int) (Math.random() * (9999999 - 1000000) + 1000000);


        String number = "VIN";
        Client current = clientRepository.findByEmail(authentication.getName());

        if(current.getAccounts().size() > 2) {
            return new ResponseEntity<>("You have more than 3 accounts", HttpStatus.FORBIDDEN);
        }

        Account account = new Account("VIN-" + random, LocalDate.now(), "0", clientRepository.findByEmail(authentication.getName()), accountType, Long.valueOf(random));
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/clients/current/account/delete")
    public ResponseEntity<?> deleteAccount(@RequestParam String number, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(number);
        System.out.println(account);

        if(account == null) {
            return new ResponseEntity<>("Cannot find the account", HttpStatus.FORBIDDEN);
        }

        List<Account> accountList = client.getAccounts().stream().filter(c -> c.getNumber().equals(number)).collect(Collectors.toList());

        if(accountList.size() < 1) {
            return new ResponseEntity<>("Cannot find the account", HttpStatus.FORBIDDEN);
        }

        transactionRepository.deleteAll(account.getTransactions());
        cardRepository.deleteById(account.getCard().getId());
        accountRepository.deleteById(account.getId());

        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }

}
