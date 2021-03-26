package com.app.bankingapp.listeners;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.exceptions.ApplicationException;

import com.app.bankingapp.repositories.JpaBankRepository;
import com.app.bankingapp.repositories.JpaCurrencyRepository;
import com.app.bankingapp.services.BankService;
import com.app.bankingapp.services.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ApplicationReadyEventListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventListener.class);

    private final JpaBankRepository BANK_REPOSITORY;
    private final JpaCurrencyRepository CURRENCY_REPOSITORY;
    private final BankService BANK_SERVICE;
    private final CurrencyService CURRENCY_SERVICE;

    public ApplicationReadyEventListener(JpaBankRepository bankRepository, JpaCurrencyRepository currencyRepository, BankService bankService, CurrencyService currencyService) {
        this.BANK_REPOSITORY = bankRepository;
        this.CURRENCY_REPOSITORY = currencyRepository;
        this.BANK_SERVICE = bankService;
        this.CURRENCY_SERVICE = currencyService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyHandler() {
// GET
        Bank bank = BANK_REPOSITORY.findById(1L)
                .orElseThrow(() -> new ApplicationException("Bank was not found"));
        LOGGER.info("Bank = {}", bank);

        BankDto bankDto = BANK_SERVICE.get(1L);
        LOGGER.info("BankDto = {}", bankDto);

        Currency currency = CURRENCY_REPOSITORY.findById(1L)
                .orElseThrow(() -> new ApplicationException("Currency was not found"));
        LOGGER.info("Currency = {}", currency);

        CurrencyDto currencyDto = CURRENCY_SERVICE.get(1L);
        LOGGER.info("CurrencyDto = {}", currencyDto);

// GET ALL
        List<Bank> banks = BANK_REPOSITORY.findAll();
        LOGGER.info("All banks = {}", banks);

        List<BankDto> bankDtos = BANK_SERVICE.getAll();
        LOGGER.info("All bank Dtos = {}", bankDtos);

        List<Currency> currencies = CURRENCY_REPOSITORY.findAll();
        LOGGER.info("All currencies = {}", currencies);

        List<CurrencyDto> currencyDtos = CURRENCY_SERVICE.getAll();
        LOGGER.info("All currency Dtos = {}", currencyDtos);

// CREATE
        Bank newBank = new Bank();
        newBank.setName("Superbank");
        newBank.setPhone("080077755");
        newBank.setType("Local");
        newBank.setAbleToBuyCurrencyOnline(false);
        newBank.setNumberOfBranches(10L);
        newBank.setAddress("Central avenue 1");
        Bank createdBank = BANK_REPOSITORY.save(newBank);
        LOGGER.info("New bank = {}", createdBank);

        BankDto newBankDto = new BankDto(newBank);
        BankDto createdBankDto = BANK_SERVICE.create(newBankDto);
        LOGGER.info("New bankDto = {}", createdBankDto);

        Currency newCurrency = new Currency();
        newCurrency.setBankId(1L);
        newCurrency.setName("Hryvnia");
        newCurrency.setShortName("UAH1");
        newCurrency.setPurchaseRate(new BigDecimal("26.11"));
        newCurrency.setSellingRate(new BigDecimal("28.12"));
        Currency createdCurrency = CURRENCY_REPOSITORY.save(newCurrency);
        LOGGER.info("New currency = {}", createdCurrency);

        CurrencyDto newCurrencyDto = new CurrencyDto(newCurrency);
        CurrencyDto createdCurrencyDto = CURRENCY_SERVICE.create(newCurrencyDto);
        LOGGER.info("New currencyDto = {}", createdCurrencyDto);

// UPDATE
        createdBank.setAddress("Central avenue 100");
        Bank updatedBank = BANK_REPOSITORY.save(createdBank);
        LOGGER.info("Updated bank = {}", updatedBank);

        createdBankDto.setAbleToBuyCurrencyOnline(true);
        BankDto updatedBankDto = BANK_SERVICE.update(createdBankDto);
        LOGGER.info("Updated bankDto = {}", updatedBankDto);

        createdCurrency.setShortName("UAH");
        Currency updatedCurrency = CURRENCY_REPOSITORY.save(createdCurrency);
        LOGGER.info("Updated currency = {}", updatedCurrency);

        createdCurrencyDto.setSellingRate(new BigDecimal("30.12"));
        CurrencyDto updatedCurrencyDto = CURRENCY_SERVICE.update(createdCurrencyDto);
        LOGGER.info("Updated bank = {}", updatedCurrencyDto);

// DELETE
        BANK_REPOSITORY.deleteById(createdBank.getId());
        banks = BANK_REPOSITORY.findAll();
        LOGGER.info("All banks (updated) = {}", banks);

        BANK_SERVICE.delete(createdBankDto.getId());
        bankDtos = BANK_SERVICE.getAll();
        LOGGER.info("All banks Dtos (updated) = {}", bankDtos);

        CURRENCY_REPOSITORY.deleteById(createdCurrency.getId());
        currencies = CURRENCY_REPOSITORY.findAll();
        LOGGER.info("All currencies (updated) = {}", currencies);

        CURRENCY_SERVICE.delete(createdCurrencyDto.getId());
        currencyDtos = CURRENCY_SERVICE.getAll();
        LOGGER.info("All currencies Dtos (updated) = {}", currencyDtos);
    }
}
