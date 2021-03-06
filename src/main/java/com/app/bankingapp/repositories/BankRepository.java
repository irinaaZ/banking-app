package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Bank;

import java.util.List;

public interface BankRepository {
    Bank create(Bank bank);
    void delete(Long id);
    Bank update(Bank bank);
    Bank get(Long id);
    List<Bank> getAll();
}
