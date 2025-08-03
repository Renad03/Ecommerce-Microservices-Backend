package com.example.microservices.shop_microservice.Payment.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;

    @NotBlank(message = "Status is required")
    private String status;

    @Transient
    private boolean fallbackTriggered;

    public Payment(Long id, Long orderId, Long userId, double amount, LocalDateTime paymentDate, String status) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public Payment() {
    }

    public Payment(Long orderId, Long userId, double amount, LocalDateTime paymentDate, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFallbackTriggered() {
        return fallbackTriggered;
    }

    public void setFallbackTriggered(boolean fallbackTriggered) {
        this.fallbackTriggered = fallbackTriggered;
    }

    public boolean getFallbackTriggered() {
        return fallbackTriggered;
    }
}
