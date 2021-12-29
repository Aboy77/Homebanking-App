package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    public List<Loan> getAll();
    public Loan save(Loan loan);
    public Loan getByName(String name);
}
