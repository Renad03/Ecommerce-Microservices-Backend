package com.example.microservices.wallet_microservice.transaction.model;

import com.example.microservices.wallet_microservice.wallet.model.Wallet;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String type;
    private LocalDateTime date;

    @ManyToOne
    @JsonBackReference
    private Wallet wallet;

    public Transaction(Long id, double amount, String type, LocalDateTime date, Wallet wallet) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.wallet = wallet;
    }
    public Transaction(){}

    public Transaction(double amount, String type, LocalDateTime date, Wallet wallet) {
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.wallet = wallet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
