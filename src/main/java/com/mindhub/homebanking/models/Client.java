package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String LastName;
    private String email;
    private String password;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    Set<Card> cards = new HashSet<>();


    public Client() {}

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.LastName = lastName;
        this.email = email;
        this.password = password;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addClients(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    @JsonIgnore
    public List<Loan> getClients() {
        return clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toList());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Client{" +
                "email='" + email + '\'' +
                '}';
    }
}
