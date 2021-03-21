package com.app.bankingapp.services.impl;

import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.exceptions.ResourceNotFoundException;
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

class CurrencyServiceImplTest {

    private final Long EXISTED_ID = 1L;
    private final Long NOT_EXISTED_ID = 99L;

    private final Currency CURRENCY_FROM_REPO1 = Currency
            .builder()
            .id(EXISTED_ID)
            .name("US Dollar")
            .shortName("USD")
            .build();

    private final Currency CURRENCY_FROM_REPO2 = Currency
            .builder()
            .id(EXISTED_ID)
            .name("Euro")
            .shortName("EUR")
            .build();

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Mock
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_currencyDto_shouldReturnNewDto() {
        Currency currencyWithoutId = Currency.builder().name(CURRENCY_FROM_REPO2.getName()).build();
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setName(CURRENCY_FROM_REPO2.getName());

        when(currencyRepository.create(currencyWithoutId))
                .thenReturn(Optional.of(CURRENCY_FROM_REPO2));

        CurrencyDto createdCurrencyDto = currencyService.create(currencyDto);

        assertNotNull(createdCurrencyDto);
        assertNotNull(createdCurrencyDto.getId());
        assertEquals(new CurrencyDto(CURRENCY_FROM_REPO2), createdCurrencyDto);

        verify(currencyRepository).create(currencyWithoutId);
    }

    @Test
    void delete_existedCurrency_shouldCallRepositoryDeleteMethod() {
        when(currencyRepository.get(EXISTED_ID)).thenReturn(Optional.of(new Currency()));

        currencyService.delete(EXISTED_ID);

        verify(currencyRepository).delete(EXISTED_ID);
    }

    @Test
    void delete_notExistedCurrency_shouldThrowResourceNotFoundException() {
        when(currencyRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () -> currencyService.delete(NOT_EXISTED_ID));

        assertEquals("Currency with id " + NOT_EXISTED_ID + " was not found", resourceNotFoundException.getMessage());

        verify(currencyRepository, never()).delete(anyLong());
    }

    @Test
    void update_existedCurrency_shouldReturnUpdatedDto() {
        String newName = "Ukrainian hryvnia";
        Currency currencyForUpdate = Currency.builder().id(EXISTED_ID).name("Ukrainian").build();
        Currency updatedCurrency = Currency.builder().id(EXISTED_ID).name(newName).build();

        when(currencyRepository.update(currencyForUpdate)).thenReturn(Optional.of(updatedCurrency));

        CurrencyDto currencyDto = currencyService.update(new CurrencyDto(currencyForUpdate));

        assertEquals(new CurrencyDto(updatedCurrency), currencyDto);
        assertEquals(newName, currencyDto.getName());

        verify(currencyRepository).update(currencyForUpdate);
    }

    @Test
    void get_existedCurrency_shouldReturnCurrencyDto() {
        when(currencyRepository.get(CURRENCY_FROM_REPO1.getId())).thenReturn(Optional.of(CURRENCY_FROM_REPO1));

        CurrencyDto currencyDto = currencyService.get(CURRENCY_FROM_REPO1.getId());

        assertNotNull(currencyDto);
        assertEquals(new CurrencyDto(CURRENCY_FROM_REPO1), currencyDto);

        verify(currencyRepository).get(CURRENCY_FROM_REPO1.getId());
    }

    @Test
    void get_notExistedCurrency_shouldThrowResourceNotFoundException() {
        when(currencyRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> currencyService.get(NOT_EXISTED_ID));

        assertEquals("Currency with id " + NOT_EXISTED_ID + " is not found",
                resourceNotFoundException.getMessage());
        verify(currencyRepository).get(NOT_EXISTED_ID);
    }

    @Test
    void getAll_shouldReturnListOfCurrencyDto() {
        List<CurrencyDto> expectedList = Arrays
                .asList(new CurrencyDto(CURRENCY_FROM_REPO1), new CurrencyDto(CURRENCY_FROM_REPO2));

        when(currencyRepository.getAll()).thenReturn(Arrays.asList(CURRENCY_FROM_REPO1, CURRENCY_FROM_REPO2));

        List<CurrencyDto> currencies = currencyService.getAll();

        assertTrue(currencies.size() > 0);
        assertEquals(expectedList, currencies);

        verify(currencyRepository).getAll();
    }

    @Test
    void searchTextInDB_shouldReturnCurrencyDtoList() {
        String searchText = "uSd";
        when(currencyRepository.searchTextInDB(searchText)).thenReturn(singletonList(CURRENCY_FROM_REPO1));

        List<CurrencyDto> currencyDtos = currencyService.searchTextInDB(searchText);

        assertEquals(singletonList(new CurrencyDto(CURRENCY_FROM_REPO1)), currencyDtos);
        verify(currencyRepository).searchTextInDB(searchText);
    }

    @Test
    void searchTextInDB_shouldReturnEmptyCurrencyDtoList() {
        String searchText = "88888888";
        when(currencyRepository.searchTextInDB(searchText)).thenReturn(Collections.emptyList());

        List<CurrencyDto> currencyDtos = currencyService.searchTextInDB(searchText);

        assertEquals(Collections.emptyList(), currencyDtos);
        assertEquals(0, currencyDtos.size());
        verify(currencyRepository).searchTextInDB(searchText);
    }
}