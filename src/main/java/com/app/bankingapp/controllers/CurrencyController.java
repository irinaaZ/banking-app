package com.app.bankingapp.controllers;

import com.app.bankingapp.dtos.CurrencyDto;
import com.app.bankingapp.services.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/v1/banks")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping(value = "/currencies/{id}")
    public CurrencyDto get(@PathVariable Long id) {
        return currencyService.get(id);
    }

    @GetMapping(value = "/currencies")
    public List<CurrencyDto> getAll() {
        return currencyService.getAll();
    }

    @PostMapping(value = "/{bankId}/currencies")
    @ResponseStatus(HttpStatus.CREATED)
    public CurrencyDto create(
            @PathVariable Long bankId,
            @RequestBody CurrencyDto currencyDto) {
        currencyDto.setBankId(bankId);
        return currencyService.create(currencyDto);
    }

    @DeleteMapping(value = "/currencies/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        currencyService.delete(id);
    }

    @PutMapping(value = "/{bankId}/currencies/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CurrencyDto update(
            @PathVariable Long bankId,
            @PathVariable Long id,
            @RequestBody CurrencyDto currencyDto) {
        currencyDto.setBankId(bankId);
        currencyDto.setId(id);
        return currencyService.update(currencyDto);
    }
}
