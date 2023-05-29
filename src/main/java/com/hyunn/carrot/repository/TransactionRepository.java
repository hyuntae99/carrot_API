package com.hyunn.carrot.repository;


import com.hyunn.carrot.entity.KakaoPay.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}