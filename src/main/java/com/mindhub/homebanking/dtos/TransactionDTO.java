package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDate;

public class TransactionDTO {
    private long id;
    private TransactionType type;
    private double amount;
    private LocalDate date;
    private String description;

    public TransactionDTO() {}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
