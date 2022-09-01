package com.mindhub.homebanking.services.implementation.impl;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.AccountRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import com.mindhub.homebanking.services.implementation.AccountService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class AccountImpl implements AccountService {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;
    
    @Override
    public int createAccNumber() {
        int random = (int) (Math.random() * (9999999 - 1000000) + 1000000);
        Account numberExist = accountRepository.findByNumber(String.valueOf(random));

        if(numberExist != null) {
            createAccNumber();
        }

        return random;
    }
}
