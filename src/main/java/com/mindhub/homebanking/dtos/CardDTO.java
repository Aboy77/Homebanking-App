package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColorType;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {
    private Long id;
    private CardType type;
    private CardColorType color;
    private long number;
    private int cvu;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private Client client;
    private String cardHolder;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvu = card.getCvu();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setNumber(long number) {
        this.number = number;
    }
}
