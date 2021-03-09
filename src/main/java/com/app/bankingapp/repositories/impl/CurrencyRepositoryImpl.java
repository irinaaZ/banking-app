package com.app.bankingapp.repositories.impl;

import com.app.bankingapp.domain.Currency;
import com.app.bankingapp.repositories.CurrencyRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CurrencyRepositoryImpl implements CurrencyRepository {

    private final RowMapper<Currency> currencyRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public CurrencyRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Currency> currencyRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.currencyRowMapper = currencyRowMapper;
    }

    @Override
    public Optional<Currency> create(Currency currency) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement("INSERT INTO bank.public.currencies (bank_id, name, short_name, purchase_rate, selling_rate) " +
                            "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, currency.getBankId());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getShortName());
            preparedStatement.setBigDecimal(4, currency.getPurchaseRate());
            preparedStatement.setBigDecimal(5, currency.getSellingRate());
            return preparedStatement;
        }, generatedKeyHolder);
        return get((Long) generatedKeyHolder.getKeys().get("id"));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM bank.public.currencies WHERE id = ?", id);
    }

    @Override
    public Optional<Currency> update(Currency currency) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement("UPDATE bank.public.currencies SET bank_id = ?, name = ?, short_name = ?, " +
                            "purchase_rate = ?, selling_rate = ? WHERE id = ?");
            preparedStatement.setLong(1, currency.getBankId());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getShortName());
            preparedStatement.setBigDecimal(4, currency.getPurchaseRate());
            preparedStatement.setBigDecimal(5, currency.getSellingRate());
            preparedStatement.setLong(6, currency.getId());
            return preparedStatement;
        });
        return get(currency.getId());
    }

    @Override
    public Optional<Currency> get(Long id) {
        try {
            return Optional
                    .ofNullable(jdbcTemplate.queryForObject("SELECT * FROM bank.public.currencies WHERE id = ?",
                            currencyRowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Currency> getAll() {
        return jdbcTemplate.query("SELECT * FROM bank.public.currencies", currencyRowMapper);
    }

    @Override
    public Set<String> getCurrenciesWithHighPurchaseRate() {
        return getAll()
                .stream()
                .filter(currency -> currency.getPurchaseRate().compareTo(new BigDecimal("26.00")) > 0)
                .map(Currency::getName)
                .collect(Collectors.toSet());
    }
}
