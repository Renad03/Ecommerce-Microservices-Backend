package com.example.microservices.wallet_microservice.transaction.controller;

import com.example.microservices.wallet_microservice.common.exception.InvalidTransactionException;
import com.example.microservices.wallet_microservice.common.exception.ResourceNotFoundException;
import com.example.microservices.wallet_microservice.common.response.ApiResponse;
import com.example.microservices.wallet_microservice.transaction.model.Transaction;
import com.example.microservices.wallet_microservice.transaction.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction saved = transactionService.saveTransaction(transaction);
            return ResponseEntity.status(201).body(
                    new ApiResponse("Transaction created successfully", 201, saved)
            );
        } catch (InvalidTransactionException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(e.getMessage(), 400, null)
            );
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(new ApiResponse("All transactions fetched", 200, transactions));
    }

    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<ApiResponse> getByWalletId(@PathVariable Long walletId) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByWalletId(walletId);
            return ResponseEntity.ok(new ApiResponse("Transactions fetched by walletId", 200, transactions));
        } catch (ResourceNotFoundException | InvalidTransactionException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), 404, null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        try {
            Transaction tx = transactionService.getTransactionById(id);
            return ResponseEntity.ok(new ApiResponse("Transaction fetched", 200, tx));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), 404, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok(new ApiResponse("Transaction deleted", 200, null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), 404, null));
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse> getByType(@PathVariable String type) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByType(type);
            return ResponseEntity.ok(new ApiResponse("Transactions by type fetched", 200, transactions));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ApiResponse(e.getMessage(), 404, null));
        }
    }

    @GetMapping("/between")
    public ResponseEntity<ApiResponse> getBetweenDates(@RequestParam("start") String start,
                                                       @RequestParam("end") String end) {
        try {
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            List<Transaction> transactions = transactionService.getTransactionsBetween(startDate, endDate);
            return ResponseEntity.ok(new ApiResponse("Transactions in date range fetched", 200, transactions));
        } catch (InvalidTransactionException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), 400, null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Invalid date format. Use ISO_LOCAL_DATE_TIME (e.g. 2025-07-26T20:00:00)", 500, null));
        }
    }
}
