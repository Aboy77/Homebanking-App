package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoanImplement implements LoanService {

    @Autowired
    public LoanRepository loanRepository;

    @Override
    public List<Loan> getAll() {
        return null;
    }

    @Override
    public Loan save(Loan loan) {
        return null;
    }

    @Override
    public Loan getByName(String name) {
        return null;
    }
}
