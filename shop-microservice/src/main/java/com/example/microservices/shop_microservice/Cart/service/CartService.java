package com.example.microservices.shop_microservice.Cart.service;

import com.example.microservices.shop_microservice.Cart.repository.CartRepository;
import com.example.microservices.shop_microservice.Cart.model.Cart;
import com.example.microservices.shop_microservice.Product.repository.ProductRepository;
import com.example.microservices.shop_microservice.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepo;
    @Autowired
    private ProductRepository productRepo;
    @Transactional
    public Cart addToCart(Cart item) {
        if (item == null) {
            throw new IllegalArgumentException("Cart item must not be null");
        }

        if (item.getProductId() == null || !productRepo.existsById(item.getProductId())) {
            throw new ResourceNotFoundException("Product with ID " + item.getProductId() + " does not exist");
        }

        if (item.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Check if product already exists in user's cart
        Optional<Cart> existingCartItem = cartRepo.findByUserIdAndProductId(item.getUserId(), item.getProductId());

        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + item.getQuantity());
            return cartRepo.save(cart);
        }

        // Otherwise, save as a new item
        return cartRepo.save(item);
    }

    @Transactional
    public boolean removeFromCart(Long userId, Long productId) {
        Optional<Cart> cartItem = cartRepo.findByUserIdAndProductId(userId, productId);

        if (cartItem.isPresent()) {
            cartRepo.delete(cartItem.get());
            return true;
        } else {
            throw new RuntimeException("Product not found in user's cart");
        }
    }



    public List<Cart> getUserCart(Long userId) {
        return cartRepo.findByUserId(userId);
    }
    @Transactional
    public void clearCart(Long userId) {
        cartRepo.deleteAll(cartRepo.findByUserId(userId));
    }
}

