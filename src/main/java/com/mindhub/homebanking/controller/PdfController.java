package com.mindhub.homebanking.controller;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.repository.TransactionRepository;
import com.mindhub.homebanking.services.implementation.impl.PdfServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@RestController
public class PdfController {
    @Autowired
    private PdfServiceImpl userServices;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private ClientRepository clientRepository;



    @GetMapping("/pdf/transactions/download")
    public void exportToPDF(HttpServletResponse response, Authentication authentication, @RequestParam Long number) throws DocumentException, IOException {

        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findAccountById(number);

        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Set<Transaction> listUsers= account.getTransactions();

        this.userServices.exports( response, listUsers, client);
    }
}
