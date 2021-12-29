package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    @OneToOne(mappedBy = "account")
    private Card card;


    private String number;
    private  LocalDate creationDate;
    private String balance;
    private AccountType accountType;
    private Long cbu;

    public Account() {}

    public Account(String number, LocalDate creationDate, String balance, Client client, AccountType accountType, Long cbu) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.accountType = accountType;
        this.cbu = cbu;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void addCard(Card card) {
        card.setAccount(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", client=" + client +
                ", transactions=" + transactions +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance='" + balance + '\'' +
                '}';
    }
}
