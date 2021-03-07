package com.app.bankingapp.configuration;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.domain.Currency;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

@Configuration
public class AppConfig {

    @Bean
    public RowMapper<Bank> bankRowMapper() {
        return (ResultSet result, int rowNum) -> {
            Bank bank = new Bank();
            bank.setId(result.getLong("id"));
            bank.setName(result.getString("name"));
            bank.setPhone(result.getString("phone"));
            bank.setType(result.getString("type"));
            bank.setAbleToBuyCurrencyOnline(result.getBoolean("able_to_buy_currency_online"));
            bank.setNumberOfBranches(result.getLong("number_of_branches"));
            bank.setAddress(result.getString("address"));
            return bank;
        };
    }

    @Bean
    public RowMapper<Currency> currencyRowMapper() {
        return (ResultSet result, int rowNum) -> {
            Currency currency = new Currency();
            currency.setId(result.getLong("id"));
            currency.setBankId(result.getLong("bank_id"));
            currency.setName(result.getString("name"));
            currency.setShortName(result.getString("name"));
            currency.setPurchaseRate(result.getBigDecimal("purchase_rate"));
            currency.setSellingRate(result.getBigDecimal("selling_rate"));
            return currency;
        };
    }
}
