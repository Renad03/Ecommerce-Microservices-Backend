package com.example.microservices.wallet_microservice.transaction.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.microservices.wallet_microservice.common.exception.InvalidTransactionException;
import com.example.microservices.wallet_microservice.common.exception.ResourceNotFoundException;
import com.example.microservices.wallet_microservice.transaction.model.Transaction;
import com.example.microservices.wallet_microservice.transaction.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction saveTransaction(Transaction transaction) {
        validateTransaction(transaction);
        transaction.setDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByWalletId(Long walletId) {
        if (walletId == null || walletId <= 0) {
            throw new InvalidTransactionException("Wallet ID must be valid and positive.");
        }

        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for wallet ID: " + walletId);
        }
        return transactions;
    }

    @Transactional(readOnly = true)
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
    }

    @Transactional
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }
        transactionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> transactions = transactionRepository.findByTypeIgnoreCase(type);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for type: " + type);
        }
        return transactions;
    }


    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || end.isBefore(start)) {
            throw new InvalidTransactionException("Invalid date range.");
        }

        List<Transaction> transactions = transactionRepository.findByDateBetween(start, end);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found in the given date range.");
        }
        return transactions;
    }


    private void validateTransaction(Transaction transaction) {
        if (transaction == null) {
            throw new InvalidTransactionException("Transaction cannot be null.");
        }
        if (transaction.getWallet().getId() == null || transaction.getWallet().getId() <= 0) {
            throw new InvalidTransactionException("Wallet ID is required and must be valid.");
        }
        if (transaction.getAmount() <= 0) {
            throw new InvalidTransactionException("Amount must be greater than zero.");
        }
        if (transaction.getType() == null || transaction.getType().isBlank()) {
            throw new InvalidTransactionException("Transaction type is required.");
        }
    }
}

