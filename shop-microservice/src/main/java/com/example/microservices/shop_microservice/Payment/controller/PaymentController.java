package com.example.microservices.shop_microservice.Payment.controller;

import com.example.microservices.shop_microservice.Payment.service.PaymentService;
import com.example.microservices.shop_microservice.Payment.model.Payment;
import com.example.microservices.shop_microservice.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/make-payment/{userId}/{orderId}")
    public ResponseEntity<ApiResponse> processPayment(@PathVariable Long userId, @PathVariable Long orderId) {
        try{ 

        Payment payment = paymentService.processPaymentSafe(userId, orderId);
        boolean isFallbackTriggered = payment.getFallbackTriggered();

        if (isFallbackTriggered) {
            String message = "Wallet service is down. Please, try again later";
            return ResponseEntity.status(400)
                .body(new ApiResponse(message,400, null));
        }

        String message = payment.getStatus().equals("SUCCESS")
                ? "Payment successful and order completed"
                : "Payment failed due to insufficient wallet balance";

        return ResponseEntity.status(payment.getStatus().equals("SUCCESS") ? 200 : 400)
                .body(new ApiResponse(message, payment.getStatus().equals("SUCCESS") ? 200 : 400, payment));
        }
        catch (RuntimeException e) {
        return ResponseEntity.status(400).body(new ApiResponse(e.getMessage(), 400, null));
        }

    }


    @GetMapping("/")
    public List<Payment> getAllPayments(){
        return paymentService.getAllPayments();
    }
}
