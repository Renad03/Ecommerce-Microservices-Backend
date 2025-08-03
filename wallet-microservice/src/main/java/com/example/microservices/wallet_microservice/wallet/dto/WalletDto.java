package com.example.microservices.wallet_microservice.wallet.dto;

    public class WalletDto {
        private Long id;
        private Long userId;
        private double balance;

        public WalletDto() {}

        public WalletDto(double balance) {
            this.balance = balance;
        }

        public WalletDto(Long id, Long userId, double balance) {
            this.id = id;
            this.userId = userId;
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
