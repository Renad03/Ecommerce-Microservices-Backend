package com.example.microservices.shop_microservice.Cart.model;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "User ID is required")
    private Long userId;

    public Cart(Long id, Long productId, int quantity, Long userId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Cart(Long productId, int quantity, Long userId) {
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Cart() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
