package com.app.bankingapp.services.impl;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
import com.app.bankingapp.repositories.BankRepository;
import com.app.bankingapp.repositories.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
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
    private BankRepository bankRepository;
    @Mock
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_bankDtoWithoutCurrencies_shouldReturnNewDtoWithoutCurrencies() {
        Bank bankWithoutId = Bank.builder().name(BANK_FROM_REPO2.getName()).build();
        BankDto bankDto = new BankDto();
        bankDto.setName(BANK_FROM_REPO2.getName());

        when(bankRepository.create(bankWithoutId))
                .thenReturn(Optional.of(BANK_FROM_REPO2));

        BankDto createdBankDto = bankService.create(bankDto);

        assertNotNull(createdBankDto);
        assertNotNull(createdBankDto.getId());
        assertEquals(new BankDto(BANK_FROM_REPO2), createdBankDto);
        assertEquals(0, createdBankDto.getCurrencies().size());

        verify(bankRepository).create(bankWithoutId);
        verify(currencyRepository, never()).create(anyList(), anyLong());
    }

    @Test
    void create_bankDtoWithCurrencies_shouldReturnNewDtoWithCurrencies() {
        Bank createdBank = Bank.builder().id(80L).name("Tur bank").build();
        Currency createdCurrency = Currency.builder().id(12L).bankId(80L).name("TRY").build();

        Bank bankWithoutId = Bank.builder().name(createdBank.getName()).build();
        Currency currencyWithoutId = Currency.builder().name(createdCurrency.getName()).build();
        Currency currencyWithBankId = Currency.builder()
                .name(createdCurrency.getName()).bankId(createdBank.getId()).build();

        when(bankRepository.create(bankWithoutId))
                .thenReturn(Optional.of(createdBank));
        when(currencyRepository.create(currencyWithBankId))
                .thenReturn(Optional.of(createdCurrency));

        BankDto createdBankDto = bankService.create(new BankDto(bankWithoutId, singletonList(currencyWithoutId)));

        assertNotNull(createdBankDto);
        assertNotNull(createdBankDto.getId());
        assertEquals(new BankDto(createdBank, singletonList(createdCurrency)), createdBankDto);
        assertEquals(1, createdBankDto.getCurrencies().size());

        verify(bankRepository).create(bankWithoutId);
        verify(currencyRepository).create(currencyWithBankId);
    }

    @Test
    void delete_existedBank_shouldCallRepositoriesDeleteMethods() {
        when(bankRepository.get(EXISTED_ID)).thenReturn(Optional.of(new Bank()));

        bankService.delete(EXISTED_ID);

        verify(bankRepository).delete(EXISTED_ID);
        verify(currencyRepository).deleteAllByBankId(EXISTED_ID);
    }

    @Test
    void delete_notExistedBank_shouldThrowResourceNotFoundException() {
        when(bankRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () -> bankService.delete(NOT_EXISTED_ID));

        assertEquals("Bank with id " + NOT_EXISTED_ID + " is not found", resourceNotFoundException.getMessage());

        verify(bankRepository, never()).delete(anyLong());
        verify(currencyRepository, never()).deleteAllByBankId(anyLong());
    }

    @Test
    void update_existedBank_shouldReturnUpdatedDto() {
        String newAddress = "Street 1000";
        Bank bankForUpdate = Bank.builder().id(EXISTED_ID).address("Street 1").build();
        Bank updatedBank = Bank.builder().id(EXISTED_ID).address(newAddress).build();

        when(bankRepository.update(bankForUpdate)).thenReturn(Optional.of(updatedBank));

        BankDto bankDto = bankService.update(new BankDto(bankForUpdate));

        assertEquals(new BankDto(updatedBank), bankDto);
        assertEquals(newAddress, bankDto.getAddress());

        verify(bankRepository).update(bankForUpdate);
    }

    @Test
    void get_existedBankWithoutCurrencies_shouldReturnBankDtoWithoutCurrencies() {
        when(bankRepository.get(BANK_FROM_REPO1.getId())).thenReturn(Optional.of(BANK_FROM_REPO1));

        BankDto bankDto = bankService.get(BANK_FROM_REPO1.getId());

        assertNotNull(bankDto);
        assertEquals(bankDto.getCurrencies().size(), 0);
        assertEquals(new BankDto(BANK_FROM_REPO1), bankDto);

        verify(bankRepository).get(BANK_FROM_REPO1.getId());
        verify(currencyRepository).getAllByBankId(BANK_FROM_REPO1.getId());
    }

    @Test
    void get_existedBankWithCurrencies_shouldReturnBankDtoWithCurrencies() {
        when(bankRepository.get(BANK_FROM_REPO1.getId()))
                .thenReturn(Optional.of(BANK_FROM_REPO1));
        when(currencyRepository.getAllByBankId(BANK_FROM_REPO1.getId()))
                .thenReturn(CURRENCIES1);

        BankDto bankDto = bankService.get(BANK_FROM_REPO1.getId());

        assertNotNull(bankDto.getCurrencies());
        assertTrue(bankDto.getCurrencies().size() > 0);
        assertEquals(new BankDto(BANK_FROM_REPO1, CURRENCIES1), bankDto);

        verify(bankRepository).get(BANK_FROM_REPO1.getId());
        verify(currencyRepository).getAllByBankId(BANK_FROM_REPO1.getId());
    }

    @Test
    void get_notExistedBank_shouldThrowResourceNotFoundException() {
        when(bankRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> bankService.get(NOT_EXISTED_ID));

        assertEquals("Bank with id " + NOT_EXISTED_ID + " is not found",
                resourceNotFoundException.getMessage());
        verify(bankRepository).get(NOT_EXISTED_ID);
    }

    @Test
    void getAll_shouldReturnListOfBankDto() {
        List<BankDto> expectedList = Arrays
                .asList(new BankDto(BANK_FROM_REPO1, CURRENCIES1), new BankDto(BANK_FROM_REPO2, CURRENCIES2));

        when(bankRepository.getAll()).thenReturn(Arrays.asList(BANK_FROM_REPO1, BANK_FROM_REPO2));
        when(currencyRepository.getAllByBankId(BANK_FROM_REPO1.getId())).thenReturn(CURRENCIES1);
        when(currencyRepository.getAllByBankId(BANK_FROM_REPO2.getId())).thenReturn(CURRENCIES2);

        List<BankDto> banks = bankService.getAll();

        assertTrue(banks.size() > 0);
        assertEquals(expectedList, banks);

        verify(bankRepository).getAll();
        verify(currencyRepository).getAllByBankId(BANK_FROM_REPO1.getId());
        verify(currencyRepository).getAllByBankId(BANK_FROM_REPO2.getId());
    }

    @Test
    void searchTextInDB_shouldReturnBankDtoList() {
        String searchText = "MoNO";
        when(bankRepository.searchTextInDB(searchText)).thenReturn(singletonList(BANK_FROM_REPO1));

        List<BankDto> bankDtos = bankService.searchTextInDB(searchText);

        assertEquals(singletonList(new BankDto(BANK_FROM_REPO1)), bankDtos);
        verify(bankRepository).searchTextInDB(searchText);
    }

    @Test
    void searchTextInDB_shouldReturnEmptyBankDtoList() {
        String searchText = "88888888";
        when(bankRepository.searchTextInDB(searchText)).thenReturn(Collections.emptyList());

        List<BankDto> bankDtos = bankService.searchTextInDB(searchText);

        assertEquals(Collections.emptyList(), bankDtos);
        assertEquals(0, bankDtos.size());
        verify(bankRepository).searchTextInDB(searchText);
    }
}