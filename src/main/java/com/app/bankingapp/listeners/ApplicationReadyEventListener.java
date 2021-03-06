package com.app.bankingapp.listeners;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.repositories.BankRepository;
import com.app.bankingapp.repositories.CurrencyRepository;

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

    public ApplicationReadyEventListener(BankRepository bankRepository, CurrencyRepository currencyRepository) {
        this.bankRepository = bankRepository;
        this.currencyRepository = currencyRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyHandler() {
// GET
        Bank bank = bankRepository.get(1L);
        LOGGER.info("Bank = {}", bank);

        Currency currency = currencyRepository.get(1L);
        LOGGER.info("Currency = {}", currency);

// GET ALL
        List<Bank> banks = bankRepository.getAll();
        LOGGER.info("All banks = {}", banks);

        List<Currency> currencies = currencyRepository.getAll();
        LOGGER.info("All currencies = {}", currencies);

// CREATE
        Bank newBank = new Bank();
        newBank.setName("Superbank");
        newBank.setPhone("080077755");
        newBank.setType("Local");
        newBank.setAbleToBuyCurrencyOnline(false);
        newBank.setNumberOfBranches(10L);
        newBank.setAddress("Central avenue 1");
        Bank createdBank = bankRepository.create(newBank);
        LOGGER.info("New bank = {}", createdBank);

        Currency newCurrency = new Currency();
        newCurrency.setBankId(1L);
        newCurrency.setName("Hryvnia");
        newCurrency.setShortName("UAH1");
        newCurrency.setPurchaseRate(new BigDecimal("26.11"));
        newCurrency.setSellingRate(new BigDecimal("28.12"));
        Currency createdCurrency = currencyRepository.create(newCurrency);
        LOGGER.info("New currency = {}", createdCurrency);

// UPDATE
        newBank.setAddress("Central avenue 100");
        Bank updatedBank = bankRepository.update(createdBank);
        LOGGER.info("Updated bank = {}", updatedBank);

        newCurrency.setShortName("UAH");
        Currency updatedCurrency = currencyRepository.update(createdCurrency);
        LOGGER.info("Updated currency = {}", updatedCurrency);

// DELETE
        bankRepository.delete(createdBank.getId());
        banks = bankRepository.getAll();
        LOGGER.info("All banks (updated) = {}", banks);

        currencyRepository.delete(createdCurrency.getId());
        currencies = currencyRepository.getAll();
        LOGGER.info("All currencies (updated) = {}", currencies);
    }
}
