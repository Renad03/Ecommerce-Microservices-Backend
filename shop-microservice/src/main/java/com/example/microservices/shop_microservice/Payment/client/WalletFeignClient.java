package com.example.microservices.shop_microservice.Payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.microservices.shop_microservice.Payment.dto.WalletDto;
import com.example.microservices.shop_microservice.common.response.ApiResponse;

@FeignClient(name = "wallet-microservice", configuration = FeignClientConfig.class)
public interface WalletFeignClient {
    @GetMapping("api/wallet/user/{userId}")
    ApiResponse getWallet(@PathVariable Long userId);

    @PostMapping("api/wallet/withdraw/{walletId}")
    void withdraw(@PathVariable Long walletId, @RequestBody WalletDto wallet);

    @PostMapping("api/wallet/deposit/{walletId}")
    void deposit(@PathVariable Long walletId, @RequestBody WalletDto wallet);
}
