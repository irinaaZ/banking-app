package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Currency;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CurrencyRepository {
    Optional<Currency> create(Currency currency);
    void delete(Long id);
    Optional<Currency> update(Currency currency);
    Optional<Currency> get(Long id);
    List<Currency> getAll();
    Set<String> getCurrenciesWithHighPurchaseRate();
}
