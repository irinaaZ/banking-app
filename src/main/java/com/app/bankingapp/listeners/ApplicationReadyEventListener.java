package com.app.bankingapp.listeners;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.exceptions.ApplicationException;
import com.app.bankingapp.repositories.BankRepository;
import com.app.bankingapp.repositories.CurrencyRepository;

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

    private final BankRepository bankRepository;
    private final CurrencyRepository currencyRepository;
    private final BankService bankService;
    private final CurrencyService currencyService;

    public ApplicationReadyEventListener(BankRepository bankRepository, CurrencyRepository currencyRepository, BankService bankService, CurrencyService currencyService) {
        this.bankRepository = bankRepository;
        this.currencyRepository = currencyRepository;
        this.bankService = bankService;
        this.currencyService = currencyService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyHandler() {
// GET
        Bank bank = bankRepository.get(1L)
                .orElseThrow(() -> new ApplicationException("Bank was not found"));
        LOGGER.info("Bank = {}", bank);

        BankDto bankDto = bankService.get(1L);
        LOGGER.info("BankDto = {}", bankDto);

        Currency currency = currencyRepository.get(1L)
                .orElseThrow(() -> new ApplicationException("Currency was not found"));
        LOGGER.info("Currency = {}", currency);

        CurrencyDto currencyDto = currencyService.get(1L);
        LOGGER.info("CurrencyDto = {}", currencyDto);

// GET ALL
        List<Bank> banks = bankRepository.getAll();
        LOGGER.info("All banks = {}", banks);

        List<BankDto> bankDtos = bankService.getAll();
        LOGGER.info("All bank Dtos = {}", bankDtos);

        List<Currency> currencies = currencyRepository.getAll();
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
        Bank createdBank = bankRepository.create(newBank)
                .orElseThrow(() -> new ApplicationException("Bank was not created"));
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
        Currency createdCurrency = currencyRepository.create(newCurrency)
                .orElseThrow(() -> new ApplicationException("Currency was not created"));
        LOGGER.info("New currency = {}", createdCurrency);

        CurrencyDto newCurrencyDto = new CurrencyDto(newCurrency);
        CurrencyDto createdCurrencyDto = currencyService.create(newCurrencyDto);
        LOGGER.info("New currencyDto = {}", createdCurrencyDto);

// UPDATE
        createdBank.setAddress("Central avenue 100");
        Bank updatedBank = bankRepository.update(createdBank)
                .orElseThrow(() -> new ApplicationException("Bank was not updated"));
        LOGGER.info("Updated bank = {}", updatedBank);

        createdBankDto.setAbleToBuyCurrencyOnline(true);
        BankDto updatedBankDto = bankService.update(createdBankDto);
        LOGGER.info("Updated bankDto = {}", updatedBankDto);

        createdCurrency.setShortName("UAH");
        Currency updatedCurrency = currencyRepository.update(createdCurrency)
                .orElseThrow(() -> new ApplicationException("Currency was not updated"));
        LOGGER.info("Updated currency = {}", updatedCurrency);

        createdCurrencyDto.setSellingRate(new BigDecimal("30.12"));
        CurrencyDto updatedCurrencyDto = currencyService.update(createdCurrencyDto);
        LOGGER.info("Updated bank = {}", updatedCurrencyDto);

// Get global banks
        LOGGER.info("Global banks are = {}", bankRepository.getGlobalBanks());

// Get high-purchase rate currencies
        LOGGER.info("High-purchase rate currencies are = {}", currencyRepository.getCurrenciesWithHighPurchaseRate());

// DELETE
        bankRepository.delete(createdBank.getId());
        banks = bankRepository.getAll();
        LOGGER.info("All banks (updated) = {}", banks);

        bankService.delete(createdBankDto.getId());
        bankDtos = bankService.getAll();
        LOGGER.info("All banks Dtos (updated) = {}", bankDtos);

        currencyRepository.delete(createdCurrency.getId());
        currencies = currencyRepository.getAll();
        LOGGER.info("All currencies (updated) = {}", currencies);

        currencyService.delete(createdCurrencyDto.getId());
        currencyDtos = currencyService.getAll();
        LOGGER.info("All currencies Dtos (updated) = {}", currencyDtos);

// Search text in banks table
        LOGGER.info("List of text found in banks table = {}", bankRepository.searchTextInDB("mAiN"));
        LOGGER.info("List of text found in banks table = {}", bankService.searchTextInDB("onoB"));

// Search text in currencies table
        LOGGER.info("List of text found in currencies table = {}", currencyRepository.searchTextInDB("us"));
        LOGGER.info("List of text found in currencies table = {}", currencyService.searchTextInDB("1"));
    }
}
