package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PaymentsDto;
import com.mindhub.homebanking.models.*;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public CardType type;

    @GetMapping("/cards")
    public List<CardDTO> getAll() {
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<?> createCard(Authentication authentication, @RequestParam CardType type, @RequestParam CardColorType color, @RequestParam String numberAcc) {
        //generating random numbers
        long number = (long) (Math.random() * (9999999999999999L - 1000000000000000L) + 1000000000000000L);


        int cvu = (int) (Math.random() * (999 - 100) + 100);

        if(type == null) {
            return  new ResponseEntity<>("Type field is empty", HttpStatus.FORBIDDEN);
        }

        if(color == null) {
            return new ResponseEntity<>("Select an card color", HttpStatus.FORBIDDEN);
        }

        if(numberAcc == null) {
            return new ResponseEntity<>("Select an account", HttpStatus.FORBIDDEN);
        }

        //get authenticated client
        Client current = clientRepository.findByEmail(authentication.getName());

        // get account to add
        Account account = accountRepository.findByNumber(numberAcc);

        if(account == null) {
            return new ResponseEntity<>("Error to find account", HttpStatus.FORBIDDEN);
        }

        Card cardList = account.getCard();

        if(cardList != null) {
            return new ResponseEntity<>("This account already have a card, choose another or create a new account", HttpStatus.FORBIDDEN);
        }

        //get his cards
        Set<Card> card = current.getCards();

        //separate type of cards to calculate quantity of this client
        List<CardType> typeDebit = card.stream().filter(c -> c.getType().equals(CardType.DEBIT)).map(c -> c.getType()).collect(Collectors.toList());
        List<CardType> typeCredit = card.stream().filter(c -> c.getType().equals(CardType.CREDIT)).map(c -> c.getType()).collect(Collectors.toList());



        if(type.equals(CardType.CREDIT)) {
            if(typeCredit.size() >= 3) {
                return new ResponseEntity<>("Have more than 3 credit card", HttpStatus.FORBIDDEN);
            }
        }
        if(type.equals(CardType.DEBIT)) {
            if(typeDebit.size() >= 3) {
                return new ResponseEntity<>("Have more than 3 debit card", HttpStatus.FORBIDDEN);
            }
        }


        Card create = new Card(clientRepository.findByEmail(authentication.getName()).getFirstName() + " " + clientRepository.findByEmail(authentication.getName()).getLastName(), type, color, number, cvu, LocalDate.now(), LocalDate.now().plusYears(5), clientRepository.findByEmail(authentication.getName()));
        account.addCard(create);
        cardRepository.save(create);

        return new ResponseEntity<>("Card ordered", HttpStatus.CREATED);
    }

    @DeleteMapping("/api/clients/current/cards/delete/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        List<Card> card = client.getCards().stream().filter(c -> c.getId().equals(id)).collect(Collectors.toList());

        if(card.size() < 1) {
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }

        cardRepository.deleteById(id);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/api/mindbrothets/payment")
    public ResponseEntity<?> payment(@RequestBody PaymentsDto paymentsDto, Authentication authentication) {
        //verify info of JSON
        Client client = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(paymentsDto.getNumberAcc());
        Card card = cardRepository.findByNumber(paymentsDto.getNumber());


        if(account == null) {
            return new ResponseEntity<>("Incorrect data", HttpStatus.FORBIDDEN);
        }

        if(card == null) {
            return new ResponseEntity<>("Incorrect data", HttpStatus.FORBIDDEN);
        }

        Double amount = Double.parseDouble(account.getBalance());

        if(amount < paymentsDto.getAmount()) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        //cast balance and proccess the buy

        Double total = amount - paymentsDto.getAmount();
        String totalString = String.valueOf(total);

        //save new balance at Account class
        account.setBalance(totalString);

        Transaction transaction = new Transaction(TransactionType.Debit, paymentsDto.getAmount(), paymentsDto.getDescription(), LocalDate.now());

        account.addTransaction(transaction);

        //save new balances at table
        accountRepository.save(account);
        transactionRepository.save(transaction);

        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
    }
}
