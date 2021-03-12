package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.CurrencyDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CurrencyRepository {
    Optional<Currency> create(Currency currency);
    List<Currency> create(List<CurrencyDto> currencies, Long bankId);
    void delete(Long id);
    void deleteAllByBankId(Long id);
    Optional<Currency> update(Currency currency);
    Optional<Currency> get(Long id);
    List<Currency> getAll();
    List<Currency> getAllByBankId(Long id);
    Set<String> getCurrenciesWithHighPurchaseRate();
}
