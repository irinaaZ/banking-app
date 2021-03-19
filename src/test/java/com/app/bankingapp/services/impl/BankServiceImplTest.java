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

    private final Long existedId = 1L;
    private final Long notExistedId = 99L;

    private final Bank bankFromRepo1 = Bank
            .builder()
            .id(existedId)
            .name("Monobank")
            .ableToBuyCurrencyOnline(true)
            .build();

    private final List<Currency> currencies1 = singletonList(
            Currency.builder().id(10L).bankId(bankFromRepo1.getId()).name("USD").build());

    private final Bank bankFromRepo2 = Bank
            .builder()
            .id(200L)
            .name("Superbank")
            .build();

    private final List<Currency> currencies2 = singletonList(
            Currency.builder().id(11L).bankId(bankFromRepo2.getId()).name("EUR").build());

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
        Bank bankWithoutId = Bank.builder().name(bankFromRepo2.getName()).build();
        BankDto bankDto = new BankDto();
        bankDto.setName(bankFromRepo2.getName());

        when(bankRepository.create(bankWithoutId))
                .thenReturn(Optional.of(bankFromRepo2));

        BankDto createdBankDto = bankService.create(bankDto);

        assertNotNull(createdBankDto);
        assertNotNull(createdBankDto.getId());
        assertEquals(new BankDto(bankFromRepo2), createdBankDto);
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
        when(bankRepository.get(existedId)).thenReturn(Optional.of(new Bank()));

        bankService.delete(existedId);

        verify(bankRepository).delete(existedId);
        verify(currencyRepository).deleteAllByBankId(existedId);
    }

    @Test
    void delete_notExistedBank_shouldThrowResourceNotFoundException() {
        when(bankRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () -> bankService.delete(notExistedId));

        assertEquals("Bank with id " + notExistedId + " is not found", resourceNotFoundException.getMessage());

        verify(bankRepository, never()).delete(anyLong());
        verify(currencyRepository, never()).deleteAllByBankId(anyLong());
    }

    @Test
    void update_existedBank_shouldReturnUpdatedDto() {
        String newAddress = "Street 1000";
        Bank bankForUpdate = Bank.builder().id(existedId).address("Street 1").build();
        Bank updatedBank = Bank.builder().id(existedId).address(newAddress).build();

        when(bankRepository.update(bankForUpdate)).thenReturn(Optional.of(updatedBank));

        BankDto bankDto = bankService.update(new BankDto(bankForUpdate));

        assertEquals(new BankDto(updatedBank), bankDto);
        assertEquals(newAddress, bankDto.getAddress());

        verify(bankRepository).update(bankForUpdate);
    }

    @Test
    void get_existedBankWithoutCurrencies_shouldReturnBankDtoWithoutCurrencies() {
        when(bankRepository.get(bankFromRepo1.getId())).thenReturn(Optional.of(bankFromRepo1));

        BankDto bankDto = bankService.get(bankFromRepo1.getId());

        assertNotNull(bankDto);
        assertEquals(bankDto.getCurrencies().size(), 0);
        assertEquals(new BankDto(bankFromRepo1), bankDto);

        verify(bankRepository).get(bankFromRepo1.getId());
        verify(currencyRepository).getAllByBankId(bankFromRepo1.getId());
    }

    @Test
    void get_existedBankWithCurrencies_shouldReturnBankDtoWithCurrencies() {
        when(bankRepository.get(bankFromRepo1.getId()))
                .thenReturn(Optional.of(bankFromRepo1));
        when(currencyRepository.getAllByBankId(bankFromRepo1.getId()))
                .thenReturn(currencies1);

        BankDto bankDto = bankService.get(bankFromRepo1.getId());

        assertNotNull(bankDto.getCurrencies());
        assertTrue(bankDto.getCurrencies().size() > 0);
        assertEquals(new BankDto(bankFromRepo1, currencies1), bankDto);

        verify(bankRepository).get(bankFromRepo1.getId());
        verify(currencyRepository).getAllByBankId(bankFromRepo1.getId());
    }

    @Test
    void get_notExistedBank_shouldThrowResourceNotFoundException() {
        when(bankRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> bankService.get(notExistedId));

        assertEquals("Bank with id " + notExistedId + " is not found",
                resourceNotFoundException.getMessage());
        verify(bankRepository).get(notExistedId);
    }

    @Test
    void getAll_shouldReturnListOfBankDto() {
        List<BankDto> expectedList = Arrays
                .asList(new BankDto(bankFromRepo1, currencies1), new BankDto(bankFromRepo2, currencies2));

        when(bankRepository.getAll()).thenReturn(Arrays.asList(bankFromRepo1, bankFromRepo2));
        when(currencyRepository.getAllByBankId(bankFromRepo1.getId())).thenReturn(currencies1);
        when(currencyRepository.getAllByBankId(bankFromRepo2.getId())).thenReturn(currencies2);

        List<BankDto> banks = bankService.getAll();

        assertTrue(banks.size() > 0);
        assertEquals(expectedList, banks);

        verify(bankRepository).getAll();
        verify(currencyRepository).getAllByBankId(bankFromRepo1.getId());
        verify(currencyRepository).getAllByBankId(bankFromRepo2.getId());
    }

    @Test
    void searchTextInDB_shouldReturnBankDtoList() {
        String searchText = "MoNO";
        when(bankRepository.searchTextInDB(searchText)).thenReturn(singletonList(bankFromRepo1));

        List<BankDto> bankDtos = bankService.searchTextInDB(searchText);

        assertEquals(singletonList(new BankDto(bankFromRepo1)), bankDtos);
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