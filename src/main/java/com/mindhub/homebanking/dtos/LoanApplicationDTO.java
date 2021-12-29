package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private String name;
    private double amount;
    private int payments;
    private String number;
    private String description;

    public LoanApplicationDTO(long id, String name,double amount, int payments, String number, String description) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.payments = payments;
        this.number = number;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LoanApplicationDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", payments=" + payments +
                ", number='" + number + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
