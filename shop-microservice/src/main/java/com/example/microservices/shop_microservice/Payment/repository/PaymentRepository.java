package com.example.microservices.shop_microservice.Payment.repository;

import com.example.microservices.shop_microservice.Payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
