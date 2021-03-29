package com.app.bankingapp.services.impl;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import com.app.bankingapp.repositories.JpaBankRepository;
import com.app.bankingapp.repositories.JpaCurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BankServiceImplTest {

    private final Long EXISTED_ID = 1L;
    private final Long NOT_EXISTED_ID = 99L;

    private final Bank BANK_FROM_REPO1 = Bank
            .builder()
            .id(EXISTED_ID)
            .name("Monobank")
            .ableToBuyCurrencyOnline(true)
            .build();

    private final List<Currency> CURRENCIES1 = singletonList(
            Currency.builder().id(10L).bankId(BANK_FROM_REPO1.getId()).name("USD").build());

    private final Bank BANK_FROM_REPO2 = Bank
            .builder()
            .id(200L)
            .name("Superbank")
            .build();

    private final List<Currency> CURRENCIES2 = singletonList(
            Currency.builder().id(11L).bankId(BANK_FROM_REPO2.getId()).name("EUR").build());

    @InjectMocks
    private BankServiceImpl bankService;

    @Mock
    private JpaBankRepository bankRepository;
    @Mock
    private JpaCurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_bankDtoWithoutCurrencies_shouldReturnNewDtoWithoutCurrencies() {
        Bank bankWithoutId = Bank.builder().name(BANK_FROM_REPO2.getName()).build();
        BankDto bankDto = new BankDto();
        bankDto.setName(BANK_FROM_REPO2.getName());

        when(bankRepository.save(bankWithoutId))
                .thenReturn(BANK_FROM_REPO2);

        BankDto createdBankDto = bankService.create(bankDto);

        assertNotNull(createdBankDto);
        assertNotNull(createdBankDto.getId());
        assertEquals(new BankDto(BANK_FROM_REPO2), createdBankDto);
        assertEquals(0, createdBankDto.getCurrencies().size());

        verify(bankRepository).save(bankWithoutId);
        verify(currencyRepository, never()).saveAll(anyList());
    }

    @Test
    void create_bankDtoWithCurrencies_shouldReturnNewDtoWithCurrencies() {
        Bank createdBank = Bank.builder().id(80L).name("Tur bank").build();
        Currency createdCurrency = Currency.builder().id(12L).bankId(80L).name("TRY").build();

        Bank bankWithoutId = Bank.builder().name(createdBank.getName()).build();
        Currency currencyWithoutId = Currency.builder().name(createdCurrency.getName()).build();
        Currency currencyWithBankId = Currency.builder()
                .name(createdCurrency.getName()).bankId(createdBank.getId()).build();

        when(bankRepository.save(bankWithoutId))
                .thenReturn(createdBank);
        when(currencyRepository.save(currencyWithBankId))
                .thenReturn(createdCurrency);

        BankDto createdBankDto = bankService.create(new BankDto(bankWithoutId, singletonList(currencyWithoutId)));

        assertNotNull(createdBankDto);
        assertNotNull(createdBankDto.getId());
        assertEquals(new BankDto(createdBank, singletonList(createdCurrency)), createdBankDto);
        assertEquals(1, createdBankDto.getCurrencies().size());

        verify(bankRepository).save(bankWithoutId);
        verify(currencyRepository).save(currencyWithBankId);
    }

    @Test
    void delete_existedBank_shouldCallRepositoriesDeleteMethods() {
        when(bankRepository.findById(EXISTED_ID)).thenReturn(Optional.of(new Bank()));

        bankService.delete(EXISTED_ID);

        verify(bankRepository).deleteById(EXISTED_ID);
        verify(currencyRepository).deleteAllByBankIdIs(EXISTED_ID);
    }

    @Test
    void delete_notExistedBank_shouldThrowResourceNotFoundException() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () -> bankService.delete(NOT_EXISTED_ID));

        assertEquals("Bank with id " + NOT_EXISTED_ID + " is not found", resourceNotFoundException.getMessage());

        verify(bankRepository, never()).deleteById(anyLong());
        verify(currencyRepository, never()).deleteAllByBankIdIs(anyLong());
    }

    @Test
    void update_existedBank_shouldReturnUpdatedDto() {
        String newAddress = "Street 1000";
        Bank bankForUpdate = Bank.builder().id(EXISTED_ID).address("Street 1").build();
        Bank updatedBank = Bank.builder().id(EXISTED_ID).address(newAddress).build();

        when(bankRepository.save(bankForUpdate)).thenReturn(updatedBank);

        BankDto bankDto = bankService.update(new BankDto(bankForUpdate));

        assertEquals(new BankDto(updatedBank), bankDto);
        assertEquals(newAddress, bankDto.getAddress());

        verify(bankRepository).save(bankForUpdate);
    }

    @Test
    void get_existedBankWithoutCurrencies_shouldReturnBankDtoWithoutCurrencies() {
        when(bankRepository.findById(BANK_FROM_REPO1.getId())).thenReturn(Optional.of(BANK_FROM_REPO1));

        BankDto bankDto = bankService.get(BANK_FROM_REPO1.getId());

        assertNotNull(bankDto);
        assertEquals(bankDto.getCurrencies().size(), 0);
        assertEquals(new BankDto(BANK_FROM_REPO1), bankDto);

        verify(bankRepository).findById(BANK_FROM_REPO1.getId());
        verify(currencyRepository).findAllByBankIdIs(BANK_FROM_REPO1.getId());
    }

    @Test
    void get_existedBankWithCurrencies_shouldReturnBankDtoWithCurrencies() {
        when(bankRepository.findById(BANK_FROM_REPO1.getId()))
                .thenReturn(Optional.of(BANK_FROM_REPO1));
        when(currencyRepository.findAllByBankIdIs(BANK_FROM_REPO1.getId()))
                .thenReturn(CURRENCIES1);

        BankDto bankDto = bankService.get(BANK_FROM_REPO1.getId());

        assertNotNull(bankDto.getCurrencies());
        assertTrue(bankDto.getCurrencies().size() > 0);
        assertEquals(new BankDto(BANK_FROM_REPO1, CURRENCIES1), bankDto);

        verify(bankRepository).findById(BANK_FROM_REPO1.getId());
        verify(currencyRepository).findAllByBankIdIs(BANK_FROM_REPO1.getId());
    }

    @Test
    void get_notExistedBank_shouldThrowResourceNotFoundException() {
        when(bankRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> bankService.get(NOT_EXISTED_ID));

        assertEquals("Bank with id " + NOT_EXISTED_ID + " is not found",
                resourceNotFoundException.getMessage());
        verify(bankRepository).findById(NOT_EXISTED_ID);
    }

    @Test
    void getAll_shouldReturnListOfBankDto() {
        List<BankDto> expectedList = Arrays
                .asList(new BankDto(BANK_FROM_REPO1, CURRENCIES1), new BankDto(BANK_FROM_REPO2, CURRENCIES2));

        when(bankRepository.findAll()).thenReturn(Arrays.asList(BANK_FROM_REPO1, BANK_FROM_REPO2));
        when(currencyRepository.findAllByBankIdIs(BANK_FROM_REPO1.getId())).thenReturn(CURRENCIES1);
        when(currencyRepository.findAllByBankIdIs(BANK_FROM_REPO2.getId())).thenReturn(CURRENCIES2);

        List<BankDto> banks = bankService.getAll();

        assertTrue(banks.size() > 0);
        assertEquals(expectedList, banks);

        verify(bankRepository).findAll();
        verify(currencyRepository).findAllByBankIdIs(BANK_FROM_REPO1.getId());
        verify(currencyRepository).findAllByBankIdIs(BANK_FROM_REPO2.getId());
    }
}