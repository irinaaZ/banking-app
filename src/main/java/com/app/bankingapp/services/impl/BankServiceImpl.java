package com.app.bankingapp.services.impl;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import com.app.bankingapp.repositories.JpaBankRepository;
import com.app.bankingapp.repositories.JpaCurrencyRepository;
import com.app.bankingapp.services.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final JpaBankRepository bankRepository;
    private final JpaCurrencyRepository currencyRepository;

    @Override
    public BankDto create(BankDto bankDto) {
        bankDto.setId(null);
        Bank newBank = bankRepository
                .save(BankDto.toDomain(bankDto));
        List<Currency> createdCurrencies = null;
        if (bankDto.getCurrencies() != null && !bankDto.getCurrencies().isEmpty()) {
            List<Currency> currencies = bankDto.getCurrencies()
                    .stream()
                    .peek(currencyDto -> currencyDto.setBankId(newBank.getId()))
                    .peek(currencyDto -> currencyDto.setId(null))
                    .map(currencyDto -> currencyRepository.save(CurrencyDto.toDomain(currencyDto)))
                    .collect(Collectors.toList());
            createdCurrencies = currencyRepository.saveAll(currencies);
        }
        return new BankDto(newBank, createdCurrencies);
    }

    @Override
    public void delete(Long id) {
        bankRepository.findById(id)
                .map(bank -> {
                    currencyRepository.deleteAllByBankIdIs(id);
                    bankRepository.deleteById(id);
                    return bank;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Bank with id " + id + " is not found"));
    }

    @Override
    public BankDto update(BankDto bankDto) {
        Bank updatedBank = bankRepository
                .save(BankDto.toDomain(bankDto));
        return new BankDto(updatedBank, currencyRepository.findAllByBankIdIs(updatedBank.getId()));
    }

    @Override
    public BankDto get(Long id) {
        return bankRepository.findById(id)
                .map(bank -> new BankDto(bank, currencyRepository.findAllByBankIdIs(id)))
                .orElseThrow(() -> new ResourceNotFoundException("Bank with id " + id + " is not found"));
    }

    @Override
    public List<BankDto> getAll() {
        return bankRepository
                .findAll()
                .stream()
                .map(bank -> new BankDto(bank, currencyRepository.findAllByBankIdIs(bank.getId())))
                .collect(Collectors.toList());
    }
}
