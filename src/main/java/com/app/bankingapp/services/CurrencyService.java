package com.app.bankingapp.services;

import com.app.bankingapp.dtos.CurrencyDto;

import java.util.List;

public interface CurrencyService {
    CurrencyDto create(CurrencyDto currency);
    void delete(Long id);
    CurrencyDto update(CurrencyDto currency);
    CurrencyDto get(Long id);
    List<CurrencyDto> getAll();
    List<CurrencyDto> searchTextInDB(String text);
}
