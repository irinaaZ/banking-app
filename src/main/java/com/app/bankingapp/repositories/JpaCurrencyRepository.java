package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findAllByBankIdIs(Long id);
    void deleteAllByBankIdIs(Long id);
}
