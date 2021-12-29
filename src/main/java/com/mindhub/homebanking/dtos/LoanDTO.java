package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
private long id;
private String name;
private double maxAccount;
private List<Integer> payments;
private int percent;

public LoanDTO(Loan loan) {
    this.id = loan.getId();
    this.name = loan.getName();
    this.maxAccount = loan.getMaxAmount();
    this.payments = loan.getPayments();
    this.percent = loan.getPercent();
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

    public double getMaxAccount() {
        return maxAccount;
    }

    public void setMaxAccount(double maxAccount) {
        this.maxAccount = maxAccount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
