package com.app.bankingapp.controllers;

import com.app.bankingapp.dtos.BankDto;
import com.app.bankingapp.services.BankService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/v1/banks")
public class BankController {

    private final BankService bankService;

    @GetMapping(value = "/{id}")
    public BankDto get(@PathVariable Long id) {
        return bankService.get(id);
    }

    @GetMapping
    public List<BankDto> getAll() {
        return bankService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankDto create(@RequestBody BankDto bankDto) {
        return bankService.create(bankDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bankService.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankDto update(
            @PathVariable Long id,
            @RequestBody BankDto bankDto) {
        bankDto.setId(id);
        return bankService.update(bankDto);
    }
}
