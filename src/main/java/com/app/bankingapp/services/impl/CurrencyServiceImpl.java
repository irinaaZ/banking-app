package com.app.bankingapp.services.impl;

import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import com.app.bankingapp.repositories.JpaCurrencyRepository;
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

    private final JpaCurrencyRepository currencyRepository;

    @Override
    public CurrencyDto create(CurrencyDto currency) {
        currency.setId(null);
        return new CurrencyDto(currencyRepository.save(CurrencyDto.toDomain(currency)));
    }

    @Override
    public void delete(Long id) {
        currencyRepository.findById(id)
                .map(currency -> {
                    currencyRepository.deleteById(id);
                    return currency;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Currency with id " + id + " was not found"));
    }

    @Override
    public CurrencyDto update(CurrencyDto currency) {
        return new CurrencyDto(currencyRepository.save(CurrencyDto.toDomain(currency)));
    }

    @Override
    public CurrencyDto get(Long id) {
        return currencyRepository
                .findById(id)
                .map(CurrencyDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("Currency with id " + id + " is not found"));
    }

    @Override
    public List<CurrencyDto> getAll() {
        return currencyRepository
                .findAll()
                .stream()
                .map(CurrencyDto::new)
                .collect(Collectors.toList());
    }
}
