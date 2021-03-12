package com.app.bankingapp.services.impl;

import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.exceptions.ApplicationException;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import com.app.bankingapp.repositories.CurrencyRepository;
import com.app.bankingapp.services.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public CurrencyDto create(CurrencyDto currency) {
        return currencyRepository
                .create(CurrencyDto.toDomain(currency))
                .map(CurrencyDto::new)
                .orElseThrow(() -> new ApplicationException("Currency " + currency + " was not added"));
    }

    @Override
    public void delete(Long id) {
        currencyRepository.delete(id);
    }

    @Override
    public CurrencyDto update(CurrencyDto currency) {
        return currencyRepository
                .update(CurrencyDto.toDomain(currency))
                .map(CurrencyDto::new)
                .orElseThrow(() -> new ApplicationException("Currency " + currency + " was not updated"));
    }

    @Override
    public CurrencyDto get(Long id) {
        return currencyRepository
                .get(id)
                .map(CurrencyDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("Currency with id " + id + " is not found"));
    }

    @Override
    public List<CurrencyDto> getAll() {
        return currencyRepository
                .getAll()
                .stream()
                .map(CurrencyDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CurrencyDto> searchTextInDB(String text) {
        return currencyRepository
                .searchTextInDB(text)
                .stream()
                .map(CurrencyDto::new)
                .collect(Collectors.toList());
    }
}
