package com.example.microservices.shop_microservice.Payment.dto;

public class WalletDto {
    private Long id;
    private Long userId;
    private double balance;

    public WalletDto() {}

    public WalletDto(double balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

