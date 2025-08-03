package com.example.microservices.wallet_microservice.wallet.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.wallet_microservice.common.response.ApiResponse;
import com.example.microservices.wallet_microservice.wallet.dto.WalletDto;
import com.example.microservices.wallet_microservice.wallet.model.Wallet;
import com.example.microservices.wallet_microservice.wallet.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/deposit/{walletId}")
    public ResponseEntity<ApiResponse> deposit(@PathVariable Long walletId, @RequestBody WalletDto walletDto) {
        double amount = walletDto.getBalance();
        walletService.deposit(walletId, amount);
        ApiResponse response = new ApiResponse("Deposit successful", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/withdraw/{walletId}")
    public ResponseEntity<ApiResponse> withdraw(@PathVariable Long walletId, @RequestBody WalletDto walletDto) {
        double amount = walletDto.getBalance();
        walletService.withdraw(walletId, amount);
        ApiResponse response = new ApiResponse("Withdrawal successful", HttpStatus.OK.value(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllWallets() {
        List<Wallet> wallets = walletService.getAllWallets();
        ApiResponse response = new ApiResponse("Wallets retrieved successfully", HttpStatus.OK.value(), wallets);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getWalletByUserId(@PathVariable Long userId) {
        Wallet wallet = walletService.getWalletByUserId(userId);
        Long id = wallet.getUser() != null ? wallet.getUser().getId() : null;
        WalletDto dto = new WalletDto(wallet.getId(), id, wallet.getBalance());
        System.out.println("Wallet balance: " + wallet.getBalance());
        ApiResponse response = new ApiResponse("Wallet retrieved successfully", HttpStatus.OK.value(), dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
