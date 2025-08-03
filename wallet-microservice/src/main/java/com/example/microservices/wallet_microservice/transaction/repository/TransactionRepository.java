package com.example.microservices.wallet_microservice.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.wallet_microservice.transaction.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(Long walletId);

    List<Transaction> findByTypeIgnoreCase(String type);

    List<Transaction> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
