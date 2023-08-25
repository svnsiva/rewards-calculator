package com.charter.rewards.calculator.repository;

import com.charter.rewards.calculator.repository.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions,Long> {
    List<Transactions> findAllByUserIdAndDateAfter (Long userId, Date date);
}
