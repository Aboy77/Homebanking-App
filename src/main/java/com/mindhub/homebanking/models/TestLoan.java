package com.mindhub.homebanking.models;

public class TestLoan {
    private double amount;
    private String name;

    public TestLoan() {}

    public TestLoan(double amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestLoan{" +
                "amount=" + amount +
                ", name='" + name + '\'' +
                '}';
    }
}
