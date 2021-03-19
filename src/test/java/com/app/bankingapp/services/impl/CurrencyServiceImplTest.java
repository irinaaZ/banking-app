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

    private final Long existedId = 1L;
    private final Long notExistedId = 99L;

    private final Currency currencyFromRepo1 = Currency
            .builder()
            .id(existedId)
            .name("US Dollar")
            .shortName("USD")
            .build();

    private final Currency currencyFromRepo2 = Currency
            .builder()
            .id(existedId)
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
        Currency currencyWithoutId = Currency.builder().name(currencyFromRepo2.getName()).build();
        CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setName(currencyFromRepo2.getName());

        when(currencyRepository.create(currencyWithoutId))
                .thenReturn(Optional.of(currencyFromRepo2));

        CurrencyDto createdCurrencyDto = currencyService.create(currencyDto);

        assertNotNull(createdCurrencyDto);
        assertNotNull(createdCurrencyDto.getId());
        assertEquals(new CurrencyDto(currencyFromRepo2), createdCurrencyDto);

        verify(currencyRepository).create(currencyWithoutId);
    }

    @Test
    void delete_existedCurrency_shouldCallRepositoryDeleteMethod() {
        when(currencyRepository.get(existedId)).thenReturn(Optional.of(new Currency()));

        currencyService.delete(existedId);

        verify(currencyRepository).delete(existedId);
    }

    @Test
    void delete_notExistedCurrency_shouldThrowResourceNotFoundException() {
        when(currencyRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException =
                assertThrows(ResourceNotFoundException.class, () -> currencyService.delete(notExistedId));

        assertEquals("Currency with id " + notExistedId + " was not found", resourceNotFoundException.getMessage());

        verify(currencyRepository, never()).delete(anyLong());
    }

    @Test
    void update_existedCurrency_shouldReturnUpdatedDto() {
        String newName = "Ukrainian hryvnia";
        Currency currencyForUpdate = Currency.builder().id(existedId).name("Ukrainian").build();
        Currency updatedCurrency = Currency.builder().id(existedId).name(newName).build();

        when(currencyRepository.update(currencyForUpdate)).thenReturn(Optional.of(updatedCurrency));

        CurrencyDto currencyDto = currencyService.update(new CurrencyDto(currencyForUpdate));

        assertEquals(new CurrencyDto(updatedCurrency), currencyDto);
        assertEquals(newName, currencyDto.getName());

        verify(currencyRepository).update(currencyForUpdate);
    }

    @Test
    void get_existedCurrency_shouldReturnCurrencyDto() {
        when(currencyRepository.get(currencyFromRepo1.getId())).thenReturn(Optional.of(currencyFromRepo1));

        CurrencyDto currencyDto = currencyService.get(currencyFromRepo1.getId());

        assertNotNull(currencyDto);
        assertEquals(new CurrencyDto(currencyFromRepo1), currencyDto);

        verify(currencyRepository).get(currencyFromRepo1.getId());
    }

    @Test
    void get_notExistedCurrency_shouldThrowResourceNotFoundException() {
        when(currencyRepository.get(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException resourceNotFoundException = assertThrows(ResourceNotFoundException.class,
                () -> currencyService.get(notExistedId));

        assertEquals("Currency with id " + notExistedId + " is not found",
                resourceNotFoundException.getMessage());
        verify(currencyRepository).get(notExistedId);
    }

    @Test
    void getAll_shouldReturnListOfCurrencyDto() {
        List<CurrencyDto> expectedList = Arrays
                .asList(new CurrencyDto(currencyFromRepo1), new CurrencyDto(currencyFromRepo2));

        when(currencyRepository.getAll()).thenReturn(Arrays.asList(currencyFromRepo1, currencyFromRepo2));

        List<CurrencyDto> currencies = currencyService.getAll();

        assertTrue(currencies.size() > 0);
        assertEquals(expectedList, currencies);

        verify(currencyRepository).getAll();
    }

    @Test
    void searchTextInDB_shouldReturnCurrencyDtoList() {
        String searchText = "uSd";
        when(currencyRepository.searchTextInDB(searchText)).thenReturn(singletonList(currencyFromRepo1));

        List<CurrencyDto> currencyDtos = currencyService.searchTextInDB(searchText);

        assertEquals(singletonList(new CurrencyDto(currencyFromRepo1)), currencyDtos);
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