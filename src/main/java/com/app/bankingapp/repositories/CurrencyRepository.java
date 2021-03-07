package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Currency;

import java.util.List;

public interface CurrencyRepository {
    Currency create(Currency currency);
    void delete(Long id);
    Currency update(Currency currency);
    Currency get(Long id);
    List<Currency> getAll();
}
