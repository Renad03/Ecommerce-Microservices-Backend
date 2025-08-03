package com.example.microservices.shop_microservice.Payment.service;

// Update the import path below to the correct location of Order.java
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.microservices.shop_microservice.Order.model.Order;
import com.example.microservices.shop_microservice.Order.repository.OrderRepository;
import com.example.microservices.shop_microservice.Payment.client.WalletFeignClient;
import com.example.microservices.shop_microservice.Payment.dto.WalletDto;
import com.example.microservices.shop_microservice.Payment.model.Payment;
import com.example.microservices.shop_microservice.Payment.repository.PaymentRepository;
import com.example.microservices.shop_microservice.common.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class PaymentService {
    @Autowired
    private WalletFeignClient walletFeignClient;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;
    
    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public Payment processPaymentSafe(Long userId, Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    if(order.getStatus().equals("COMPLETED")) {
        System.out.println("Order is already completed");
        throw new RuntimeException("Order is already completed");
    }
    return processPayment(userId, order); 
   }


    @CircuitBreaker(name = "walletService", fallbackMethod = "fallbackWallet")
    @Transactional
    public Payment processPayment(Long userId, Order order) {
        double totalAmount = order.getTotalAmount();
        ApiResponse response = walletFeignClient.getWallet(userId);
        Object data = response.getData();
        System.out.println("User wallet data in process payment function: " + data);
        WalletDto wallet = convertToWalletDto(data);
        System.out.println("User wallet in process payment function: " + wallet.getUserId());
        double walletBalance = wallet.getBalance();
        Long walletId = wallet.getId();
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setUserId(userId);
        payment.setAmount(totalAmount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setFallbackTriggered(false);
        if (walletBalance >= totalAmount) {
            // Deduct amount from wallet
            walletFeignClient.withdraw(walletId, new WalletDto(totalAmount));
            payment.setStatus("SUCCESS");
            order.setStatus("COMPLETED");
            orderRepository.save(order);
        } else {
            System.out.println("FAILED");
            payment.setStatus("FAILED");
        }

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment fallbackWallet(Long userId, Order order, Throwable ex) {
        System.err.println("Wallet service is down. Please, try again later" + ex.getMessage());

        Payment failedPayment = new Payment();
        failedPayment.setOrderId(order.getId());
        failedPayment.setUserId(userId);
        failedPayment.setAmount(order.getTotalAmount());
        failedPayment.setPaymentDate(LocalDateTime.now());
        failedPayment.setStatus("FAILED");
        failedPayment.setFallbackTriggered(true);
        return paymentRepository.save(failedPayment);
    }

    @CircuitBreaker(name = "walletService", fallbackMethod = "fallbackRefund")
    @Transactional
    public void refund(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        Long userId = order.getUserId();
        double amount = order.getTotalAmount();

        ApiResponse response = walletFeignClient.getWallet(userId);
        Object data = response.getData();
        WalletDto wallet = convertToWalletDto(data);
        Long walletId = wallet.getId();

        // Deposit refund amount back to the wallet
        walletFeignClient.deposit(walletId, new WalletDto(amount));

        // Save refund payment record
        Payment refundPayment = new Payment();
        refundPayment.setOrderId(order.getId());
        refundPayment.setUserId(userId);
        refundPayment.setAmount(amount);
        refundPayment.setPaymentDate(LocalDateTime.now());
        refundPayment.setStatus("REFUNDED");

        paymentRepository.save(refundPayment);
    }

    @Transactional
    public Payment fallbackRefund(Long orderId, Throwable throwable) {
        System.err.println("Refund fallback triggered: " + throwable.getMessage());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        Payment failedRefund = new Payment();
        failedRefund.setOrderId(order.getId());
        failedRefund.setUserId(order.getUserId());
        failedRefund.setAmount(order.getTotalAmount());
        failedRefund.setPaymentDate(LocalDateTime.now());
        failedRefund.setStatus("REFUND_FAILED");

        return paymentRepository.save(failedRefund);
    }

    private WalletDto convertToWalletDto(Object data) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(data, WalletDto.class);
}


}
