package com.app.bankingapp.dtos;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BankDto {
    private Long id;
    private String name;
    private String phone;
    private String type;
    private boolean ableToBuyCurrencyOnline;
    private Long numberOfBranches;
    private String address;
    private List<CurrencyDto> currencies;

    public BankDto(Bank bank, List<Currency> currencies) {
        this.id = bank.getId();
        this.name = bank.getName();
        this.phone = bank.getPhone();
        this.type = bank.getType();
        this.ableToBuyCurrencyOnline = bank.isAbleToBuyCurrencyOnline();
        this.numberOfBranches = bank.getNumberOfBranches();
        this.address = bank.getAddress();
        this.currencies = CollectionUtils.isEmpty(currencies) ? Collections.emptyList() : currencies
                .stream()
                .map(CurrencyDto::new)
                .collect(Collectors.toList());
    }

    public BankDto(Bank bank) {
        this(bank, Collections.emptyList());
    }

    public static Bank toDomain(BankDto bankDto) {
        Bank bank = new Bank();
        bank.setId(bankDto.getId());
        bank.setName(bankDto.getName());
        bank.setPhone(bankDto.getPhone());
        bank.setType(bankDto.getType());
        bank.setAbleToBuyCurrencyOnline(bankDto.isAbleToBuyCurrencyOnline());
        bank.setNumberOfBranches(bankDto.getNumberOfBranches());
        bank.setAddress(bankDto.getAddress());
        return bank;
    }
}
