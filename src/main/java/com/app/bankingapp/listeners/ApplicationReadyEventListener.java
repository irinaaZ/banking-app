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

    private final JpaBankRepository bankRepository;
    private final JpaCurrencyRepository currencyRepository;
    private final BankService bankService;
    private final CurrencyService currencyService;

    public ApplicationReadyEventListener(JpaBankRepository bankRepository, JpaCurrencyRepository currencyRepository, BankService bankService, CurrencyService currencyService) {
        this.bankRepository = bankRepository;
        this.currencyRepository = currencyRepository;
        this.bankService = bankService;
        this.currencyService = currencyService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyHandler() {
// GET
        Bank bank = bankRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException("Bank was not found"));
        LOGGER.info("Bank = {}", bank);

        BankDto bankDto = bankService.get(1L);
        LOGGER.info("BankDto = {}", bankDto);

        Currency currency = currencyRepository.findById(1L)
                .orElseThrow(() -> new ApplicationException("Currency was not found"));
        LOGGER.info("Currency = {}", currency);

        CurrencyDto currencyDto = currencyService.get(1L);
        LOGGER.info("CurrencyDto = {}", currencyDto);

// GET ALL
        List<Bank> banks = bankRepository.findAll();
        LOGGER.info("All banks = {}", banks);

        List<BankDto> bankDtos = bankService.getAll();
        LOGGER.info("All bank Dtos = {}", bankDtos);

        List<Currency> currencies = currencyRepository.findAll();
        LOGGER.info("All currencies = {}", currencies);

        List<CurrencyDto> currencyDtos = currencyService.getAll();
        LOGGER.info("All currency Dtos = {}", currencyDtos);

// CREATE
        Bank newBank = new Bank();
        newBank.setName("Superbank");
        newBank.setPhone("080077755");
        newBank.setType("Local");
        newBank.setAbleToBuyCurrencyOnline(false);
        newBank.setNumberOfBranches(10L);
        newBank.setAddress("Central avenue 1");
        Bank createdBank = bankRepository.save(newBank);
        LOGGER.info("New bank = {}", createdBank);

        BankDto newBankDto = new BankDto(newBank);
        BankDto createdBankDto = bankService.create(newBankDto);
        LOGGER.info("New bankDto = {}", createdBankDto);

        Currency newCurrency = new Currency();
        newCurrency.setBankId(1L);
        newCurrency.setName("Hryvnia");
        newCurrency.setShortName("UAH1");
        newCurrency.setPurchaseRate(new BigDecimal("26.11"));
        newCurrency.setSellingRate(new BigDecimal("28.12"));
        Currency createdCurrency = currencyRepository.save(newCurrency);
        LOGGER.info("New currency = {}", createdCurrency);

        CurrencyDto newCurrencyDto = new CurrencyDto(newCurrency);
        CurrencyDto createdCurrencyDto = currencyService.create(newCurrencyDto);
        LOGGER.info("New currencyDto = {}", createdCurrencyDto);

// UPDATE
        createdBank.setAddress("Central avenue 100");
        Bank updatedBank = bankRepository.save(createdBank);
        LOGGER.info("Updated bank = {}", updatedBank);

        createdBankDto.setAbleToBuyCurrencyOnline(true);
        BankDto updatedBankDto = bankService.update(createdBankDto);
        LOGGER.info("Updated bankDto = {}", updatedBankDto);

        createdCurrency.setShortName("UAH");
        Currency updatedCurrency = currencyRepository.save(createdCurrency);
        LOGGER.info("Updated currency = {}", updatedCurrency);

        createdCurrencyDto.setSellingRate(new BigDecimal("30.12"));
        CurrencyDto updatedCurrencyDto = currencyService.update(createdCurrencyDto);
        LOGGER.info("Updated bank = {}", updatedCurrencyDto);

// DELETE
        bankRepository.deleteById(createdBank.getId());
        banks = bankRepository.findAll();
        LOGGER.info("All banks (updated) = {}", banks);

        bankService.delete(createdBankDto.getId());
        bankDtos = bankService.getAll();
        LOGGER.info("All banks Dtos (updated) = {}", bankDtos);

        currencyRepository.deleteById(createdCurrency.getId());
        currencies = currencyRepository.findAll();
        LOGGER.info("All currencies (updated) = {}", currencies);

        currencyService.delete(createdCurrencyDto.getId());
        currencyDtos = currencyService.getAll();
        LOGGER.info("All currencies Dtos (updated) = {}", currencyDtos);
    }
}
