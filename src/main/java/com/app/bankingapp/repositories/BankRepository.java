package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Bank;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BankRepository {
    Optional<Bank> create(Bank bank);
    void delete(Long id);
    Optional<Bank> update(Bank bank);
    Optional<Bank> get(Long id);
    List<Bank> getAll();
    Set<String> getGlobalBanks();
}
