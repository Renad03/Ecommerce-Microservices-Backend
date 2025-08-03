package com.example.microservices.inventory_microservice.inventory.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.microservices.inventory_microservice.common.response.ApiResponse;
import com.example.microservices.inventory_microservice.inventory.client.ProductFeignClient;
import com.example.microservices.inventory_microservice.inventory.dto.ProductDto;
import com.example.microservices.inventory_microservice.inventory.model.Inventory;
import com.example.microservices.inventory_microservice.inventory.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    private ProductFeignClient productClient;

    @Autowired
    private ObjectMapper objectMapper;
    public Inventory createNewInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }
    public Inventory addStock(Inventory inventory) {
        validateProductExists(inventory.getProductId());
        return inventoryRepository.save(inventory);
    }

    public void addProduct(ProductDto product) {
        productClient.addProduct(product);
    }

    public Inventory updateStock(Long productId, int quantity) {
        validateProductExists(productId);

        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setQuantity(quantity);
            return inventoryRepository.save(inventory);
        } else {
            throw new RuntimeException("Inventory not found for productId: " + productId);
        }
    }

    public void deleteByProductId(Long productId) {
        validateProductExists(productId);
        inventoryRepository.deleteByProductId(productId);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallBackProduct")
    public void validateProductExists(Long productId) {
    ResponseEntity<ApiResponse> response = productClient.getProductById(productId);
    
    Object dataObj = response.getBody().getData();
    ProductDto product = objectMapper.convertValue(dataObj, ProductDto.class);

    System.out.println("Product in validation method: " + product.getName());

    if (product == null || product.getName() == null) {
        throw new RuntimeException("Product is null");
    }
}

    public void fallBackProduct(Long productId, Throwable ex) {
        System.err.println("Fallback triggered for product ID " + productId + ": " + ex.getMessage());
        throw new RuntimeException("Product service is currently unavailable. Please try again later.");
    }
    public void deleteById(Long inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for id: " + inventoryId));
        inventoryRepository.delete(inventory);
    }

    public Inventory getInventoryById(Long inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for id: " + inventoryId));
    }

    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }
}
