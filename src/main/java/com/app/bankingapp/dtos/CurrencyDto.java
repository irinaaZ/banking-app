package com.app.bankingapp.dtos;

import com.app.bankingapp.domain.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CurrencyDto {

    private Long id;
    private Long bankId;
    private String name;
    private String shortName;
    private BigDecimal purchaseRate;
    private BigDecimal sellingRate;

    public CurrencyDto(Currency currency) {
        this.id = currency.getId();
        this.bankId = currency.getBankId();
        this.name = currency.getName();
        this.shortName = currency.getShortName();
        this.purchaseRate = currency.getPurchaseRate();
        this.sellingRate = currency.getSellingRate();
    }

    public static Currency toDomain(CurrencyDto currencyDto) {
        Currency currency = new Currency();
        currency.setId(currencyDto.getId());
        currency.setBankId(currencyDto.getBankId());
        currency.setName(currencyDto.getName());
        currency.setShortName(currencyDto.getShortName());
        currency.setPurchaseRate(currencyDto.getPurchaseRate());
        currency.setSellingRate(currencyDto.getSellingRate());
        return currency;
    }
}
