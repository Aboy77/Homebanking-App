package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {
    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public LoanRepository loanRepository;

    @Autowired
    public ClientLoanRepository clientLoanRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    @GetMapping("/api/loans")
    public List<LoanDTO> getAll() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/api/loans")
    public ResponseEntity<?> createLoan(@RequestBody LoanApplicationDTO loans, Authentication authentication) {
        //si los campos estan vacios
        if(loans.getName().isEmpty() || loans.getNumber().isEmpty()) {
            return new ResponseEntity<>("Fields cannot are empty", HttpStatus.FORBIDDEN);
        }
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account1 = accountRepository.findByNumber(loans.getNumber());
        Loan loan = loanRepository.findByName(loans.getName());

        if(account1 == null) {
            return new ResponseEntity<>("Error: could not find a loan with this name", HttpStatus.FORBIDDEN);
        }

        if(loan == null) {
            return new ResponseEntity<>("Error: could not find the loan type", HttpStatus.FORBIDDEN);
        }

        List<Account> verify = client.getAccounts().stream().filter(acc -> acc.equals(account1)).collect(Collectors.toList());
        List<Integer> payment = loan.getPayments();
        boolean cuotaContain = payment.contains(loans.getPayments());

        double max = loan.getMaxAmount();
        int tax = (int)(loans.getAmount() * loan.getPercent()) / 100;
        double totalAmount = loans.getAmount() + tax;
        int perMonth = tax / loans.getPayments();


        //si la cuenta no existe
        if(account1.getNumber().length() < 1) {
            return new ResponseEntity<>("Cannot find the account",HttpStatus.FORBIDDEN);
        }

        //si la cuenta no pertenece al cliente
        if(verify.size() < 1) {
            return new ResponseEntity<>("The account is not from current client", HttpStatus.FORBIDDEN);
        }

        //si el monto supera al prestamo
        if(loans.getAmount() > max) {
            return new ResponseEntity<>("The amount cannot be more than the loan allowed",HttpStatus.FORBIDDEN);
        }

        //si la cuota pertenece al prestamo solicitado
        if(!cuotaContain) {
            return new ResponseEntity<>("The payment do not match with this loan", HttpStatus.FORBIDDEN);
        }
        String bln = account1.getBalance();

        Double balance = Double.parseDouble(bln);
        double total =  loans.getAmount() + balance;
        String totalBalance = String.valueOf(total);

        account1.setBalance(totalBalance);

        ClientLoan clientLoan = new ClientLoan(totalAmount, loans.getPayments(), client, loan);
        Transaction transaction = new Transaction(TransactionType.Credit, loans.getAmount(), loans.getDescription(), LocalDate.now());
        transactionRepository.save(transaction);

        account1.addTransaction(transaction);
        accountRepository.save(account1);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
