package com.mindhub.homebanking.dtos;

public class PaymentsDto {
    private long number;
    private int cvv;
    private Double amount;
    private String description;
    private String numberAcc;

    public PaymentsDto(long number, int cvv, Double amount, String description, String numberAcc) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.numberAcc = numberAcc;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNumberAcc() {
        return numberAcc;
    }

    public void setNumberAcc(String numberAcc) {
        this.numberAcc = numberAcc;
    }
}
