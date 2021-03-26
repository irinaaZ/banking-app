package com.app.bankingapp.repositories;

import com.app.bankingapp.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBankRepository extends JpaRepository<Bank, Long> {
}
