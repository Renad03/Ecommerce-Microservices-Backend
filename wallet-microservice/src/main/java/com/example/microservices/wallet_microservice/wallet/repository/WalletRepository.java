package com.example.microservices.wallet_microservice.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.wallet_microservice.wallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    public Optional<Wallet> findByUserId(Long userId);

    void deleteByUserId(Long id);
}
