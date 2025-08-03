package com.example.microservices.shop_microservice.Order.controller;

import com.example.microservices.shop_microservice.common.response.ApiResponse;
import com.example.microservices.shop_microservice.Order.service.OrderService;
import com.example.microservices.shop_microservice.Order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> createOrder(@PathVariable Long userId) {
        try {
            Order createdOrder = orderService.createOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order created successfully", 200, createdOrder));
        }catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Cannot create order: " + ex.getMessage(), 400, null));
        }
        catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("User not found", 404, null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to create order", 500, null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> orders(@PathVariable Long userId) {
        try {
            List<Order> userOrders = orderService.findByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully", 200, userOrders));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("User not found", 404, null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to retrieve orders", 500, null));
        }
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long orderId) {
        try {
            Order order = orderService.cancelOrder(orderId);

            if ("CANCELLED".equals(order.getStatus())) {
                return ResponseEntity.ok(new ApiResponse("Your order is cancelled successfully", 200, order));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse("Order could not be cancelled", 409, order));
            }

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Order not found", 404, null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong", 500, null));
        }
    }


}