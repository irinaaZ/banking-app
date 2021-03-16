package com.app.bankingapp.controllers;

import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.services.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/v1/banks/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/{id}")
    public CurrencyDto get(@PathVariable Long id) {
        return currencyService.get(id);
    }

    @GetMapping
    public List<CurrencyDto> getAll() {
        return currencyService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyDto create(@RequestBody CurrencyDto currencyDto) {
        return currencyService.create(currencyDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        currencyService.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyDto update(
            @PathVariable Long id,
            @RequestBody CurrencyDto currencyDto) {
        currencyDto.setId(id);
        return currencyService.update(currencyDto);
    }
}
