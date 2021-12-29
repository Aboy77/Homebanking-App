package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "CARD_LIST")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private CardType type;
    private CardColorType color;
    private long number;
    private int cvu;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardHolder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_client")
    private Client client;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    public Card() {}

    public Card(String cardHolder ,CardType cardType, CardColorType cardColorType, long number, int cvu, LocalDate fromDate, LocalDate thruDate, Client client) {
        this.cardHolder = cardHolder;
        this.type = cardType;
        this.color = cardColorType;
        this.number = number;
        this.cvu = cvu;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColorType getColor() {
        return color;
    }

    public void setColor(CardColorType color) {
        this.color = color;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCvu() {
        return cvu;
    }

    public void setCvu(int cvu) {
        this.cvu = cvu;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", type=" + type +
                ", color=" + color +
                ", number=" + number +
                ", cvu=" + cvu +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                ", cardHolder='" + cardHolder + '\'' +
                '}';
    }
}
