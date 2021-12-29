package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Admin;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.AdminRepository;
import com.mindhub.homebanking.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputEmail -> {
           Client client = clientRepository.findByEmail(inputEmail);
           Admin admin = adminRepository.findByEmail(inputEmail);

           if(client != null) {
               return new User(client.getEmail(), client.getPassword() , AuthorityUtils.createAuthorityList("CLIENT"));
           } else if(admin != null) {
               return new User(admin.getEmail(), admin.getPassword(), AuthorityUtils.createAuthorityList("ADMIN"));
           } else {
               throw new UsernameNotFoundException("Unknow user" + inputEmail);
           }

        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
