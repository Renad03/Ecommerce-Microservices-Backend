package com.example.microservices.wallet_microservice.wallet.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.microservices.wallet_microservice.transaction.model.Transaction;
import com.example.microservices.wallet_microservice.transaction.repository.TransactionRepository;
import com.example.microservices.wallet_microservice.wallet.model.Wallet;
import com.example.microservices.wallet_microservice.wallet.repository.WalletRepository;

@Service
public class WalletService {
    @Autowired
    WalletRepository walletRepo;
    @Autowired
    TransactionRepository transactionRepo;



    public void deposit(Long walletId, double amount) {
        Wallet wallet = walletRepo.findById(walletId).orElseThrow();
        wallet.setBalance(wallet.getBalance() + amount);
        walletRepo.save(wallet);
        transactionRepo.save(new Transaction(null, amount, "DEPOSIT", LocalDateTime.now(), wallet));
    }

    public void withdraw(Long walletId, double amount) {
        Wallet wallet = walletRepo.findById(walletId).orElseThrow();
        if (wallet.getBalance() < amount) throw new RuntimeException("Insufficient balance");
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);
        transactionRepo.save(new Transaction(null, amount, "WITHDRAW", LocalDateTime.now(), wallet));
    }

    public List<Wallet> getAllWallets(){
        return walletRepo.findAll();
    }

    public Wallet getWalletByUserId(Long userId) {
    return walletRepo.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Wallet not found for user ID: " + userId));
}

}
