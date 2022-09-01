package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.email.Mail;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repository.*;
import com.mindhub.homebanking.services.implementation.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class ClientController {

    @Autowired
    private Mail mail;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/api/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @PostMapping("/api/clients")
    public ResponseEntity<Client> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {
        int random = accountService.createAccNumber();


        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(clientRepository.findByEmail(email) != null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        Account account = new Account("VIN-" + random, LocalDate.now(), "0", client, AccountType.SAVING, Long.valueOf(random));
        accountRepository.save(account);

        mail.sendMail(email, "Welcome to Mindhub Brothers", "You account have been created successfully you user is " + email + " Login and start user you account");

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/delete")
    public ResponseEntity<?> deleteAcc(Authentication authentication) {
        // get Client and Accounts
        Client client = clientRepository.findByEmail(authentication.getName());
        Set<Account> accountSet = client.getAccounts();
        List<Account> accountList = new ArrayList<>(accountSet);

        if(client == null) {
            return new ResponseEntity<>("Fatal error code: 1C", HttpStatus.FORBIDDEN);
        }

        // get and delete cards
        Set<Card> cards = client.getCards();
        if(cards.size() > 0) {
            List<Card> cardsList = new ArrayList<>(cards);
            for(int i = 0; i < cardsList.size(); i++) {
                cardRepository.deleteById(cardsList.get(i).getId());
            }
        }

        // get and delete loans
        Set<ClientLoan> clientLoans = client.getClientLoans();
        if(clientLoans.size() > 0) {
            List<ClientLoan> clientLoanList = new ArrayList<>(clientLoans);
            for(int i = 0; i < clientLoanList.size(); i++) {
                clientLoanRepository.deleteById(clientLoanList.get(i).getId());
            }
        }

        // delete transactions and then accounts
        for(int i = 0; i < accountList.size(); i++) {
            Set<Transaction> transactionsSet = accountList.get(i).getTransactions();
            if(transactionsSet.size() > 0) {
                List<Transaction> transactionList = new ArrayList<>(transactionsSet);
                for(int j = 0; j < transactionList.size(); j ++){
                    transactionRepository.deleteById(transactionList.get(j).getId());
                }
            }
            accountRepository.deleteById(accountList.get(i).getId());
        }

        // finally, delete the Client
        clientLoanRepository.deleteById(client.getId());


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/clients/current")
    public ClientDTO getClientDto(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}
