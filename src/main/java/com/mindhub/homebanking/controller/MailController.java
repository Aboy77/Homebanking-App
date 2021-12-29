package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.email.Mail;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private Mail mail;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/form/send")
    public ResponseEntity<?> sendForm(Authentication authentication, @RequestParam String subject, @RequestParam String body) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if(subject == null) {
            return new ResponseEntity<>("Select a reason", HttpStatus.FORBIDDEN);
        }

        if(body == null) {
            return new ResponseEntity<>("Details field s empty", HttpStatus.FORBIDDEN);
        }

        mail.sendMail("brian_m.i.l@hotmail.com", "Mindhub Brothers Support", "Hello, I am sorry that you have had any inconvenience, one of our representative will contact you shortly");
        mail.sendMail("briancuenca200@gmail.com", "user report " + subject, "his message says " + body);

        return new ResponseEntity<>("Form sent", HttpStatus.ACCEPTED);
    }
}
