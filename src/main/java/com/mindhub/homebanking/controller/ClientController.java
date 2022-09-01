package com.mindhub.homebanking.controller;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.email.Mail;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.services.implementation.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

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

    @GetMapping("/api/clients/current")
    public ClientDTO getClientDto(Authentication authentication) {
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}
