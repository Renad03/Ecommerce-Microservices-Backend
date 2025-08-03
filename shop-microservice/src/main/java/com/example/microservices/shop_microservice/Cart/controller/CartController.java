package com.example.microservices.shop_microservice.Cart.controller;

import com.example.microservices.shop_microservice.Cart.service.CartService;
import com.example.microservices.shop_microservice.Cart.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> add(@RequestBody Cart item) {
        return ResponseEntity.ok(cartService.addToCart(item));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @PostMapping("/remove/{userId}/{productId}")
    public ResponseEntity<String> remove(@PathVariable Long userId, @PathVariable Long productId) {
        boolean removed = cartService.removeFromCart(userId, productId);
        if (removed) {
            return ResponseEntity.ok("Product removed from cart successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart.");
        }
    }
}
