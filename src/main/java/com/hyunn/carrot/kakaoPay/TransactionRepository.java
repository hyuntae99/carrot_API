package com.hyunn.carrot.kakaoPay;


import com.hyunn.carrot.kakaoPay.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}