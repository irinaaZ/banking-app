package com.app.bankingapp.services.impl;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.exceptions.ApplicationException;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import com.app.bankingapp.repositories.BankRepository;
import com.app.bankingapp.repositories.CurrencyRepository;
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

    private final BankRepository bankRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public BankDto create(BankDto bankDto) {
        Bank newBank = bankRepository
                .create(BankDto.toDomain(bankDto))
                .orElseThrow(() -> new ApplicationException("Bank " + bankDto + " was not added"));
        List<Currency> createdCurrencies = null;
        if (bankDto.getCurrencies() != null && !bankDto.getCurrencies().isEmpty()) {
            createdCurrencies = currencyRepository.create(bankDto.getCurrencies(), newBank.getId());
        }
        return new BankDto(newBank, createdCurrencies);
    }

    @Override
    public void delete(Long id) {
        bankRepository.get(id)
                .map(bank -> {
                    currencyRepository.deleteAllByBankId(id);
                    bankRepository.delete(id);
                    return bank;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Bank with id " + id + " is not found"));
    }

    @Override
    public BankDto update(BankDto bankDto) {
        Bank updatedBank = bankRepository
                .update(BankDto.toDomain(bankDto))
                .orElseThrow(() -> new ApplicationException("Bank " + bankDto + " was not updated"));
        return new BankDto(updatedBank, currencyRepository.getAllByBankId(updatedBank.getId()));
    }

    @Override
    public BankDto get(Long id) {
        return bankRepository.get(id)
                .map(bank -> new BankDto(bank, currencyRepository.getAllByBankId(id)))
                .orElseThrow(() -> new ResourceNotFoundException("Bank with id " + id + " is not found"));
    }

    @Override
    public List<BankDto> getAll() {
        return bankRepository
                .getAll()
                .stream()
                .map(bank -> new BankDto(bank, currencyRepository.getAllByBankId(bank.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<BankDto> searchTextInDB(String text) {
        return bankRepository
                .searchTextInDB(text)
                .stream()
                .map(BankDto::new)
                .collect(Collectors.toList());
    }
}
