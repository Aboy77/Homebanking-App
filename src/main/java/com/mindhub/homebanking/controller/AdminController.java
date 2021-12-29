package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Admin;
import com.mindhub.homebanking.repository.AdminRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/api/clients")
    public List<ClientDTO> getClients(Authentication authentication) {
        Admin admin = adminRepository.findByEmail(authentication.getName());

        return clientRepository.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
}
