package com.app.bankingapp.services;

import com.app.bankingapp.dtos.BankDto;

import java.util.List;

public interface BankService {
    BankDto create(BankDto bank);
    void delete(Long id);
    BankDto update(BankDto bank);
    BankDto get(Long id);
    List<BankDto> getAll();
    List<BankDto> searchTextInDB(String text);
}
