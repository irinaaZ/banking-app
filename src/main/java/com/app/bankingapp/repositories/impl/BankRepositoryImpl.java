package com.app.bankingapp.repositories.impl;

import com.app.bankingapp.domain.Bank;
import com.app.bankingapp.repositories.BankRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class BankRepositoryImpl implements BankRepository {

    private final RowMapper<Bank> bankRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public BankRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Bank> bankRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bankRowMapper = bankRowMapper;
    }

    @Override
    public Bank create(Bank bank) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement("INSERT INTO bank.public.banks (name, phone, type, able_to_buy_currency_online, number_of_branches, address) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getPhone());
            preparedStatement.setString(3, bank.getType());
            preparedStatement.setBoolean(4, bank.isAbleToBuyCurrencyOnline());
            preparedStatement.setLong(5, bank.getNumberOfBranches());
            preparedStatement.setString(6, bank.getAddress());
            return preparedStatement;
        }, generatedKeyHolder);
        return get((Long) generatedKeyHolder.getKeys().get("id"));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM bank.public.banks WHERE id = ?", id);
    }

    @Override
    public Bank update(Bank bank) {
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement =
                    con.prepareStatement("UPDATE bank.public.banks SET name = ?, phone = ?, type = ?, " +
                            "able_to_buy_currency_online = ?, number_of_branches = ?, address = ? WHERE id = ?");
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getPhone());
            preparedStatement.setString(3, bank.getType());
            preparedStatement.setBoolean(4, bank.isAbleToBuyCurrencyOnline());
            preparedStatement.setLong(5, bank.getNumberOfBranches());
            preparedStatement.setString(6, bank.getAddress());
            preparedStatement.setLong(7, bank.getId());
            return preparedStatement;
        });
        return get(bank.getId());
    }

    @Override
    public Bank get(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM bank.public.banks WHERE id = ?",
                bankRowMapper, id);
    }

    @Override
    public List<Bank> getAll() {
        return jdbcTemplate.query("SELECT * FROM bank.public.banks", bankRowMapper);
    }
}
