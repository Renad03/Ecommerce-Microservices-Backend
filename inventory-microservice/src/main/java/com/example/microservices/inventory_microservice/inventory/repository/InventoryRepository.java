package com.example.microservices.inventory_microservice.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.inventory_microservice.inventory.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    public Optional<Inventory> findByProductId(Long productId);
    public void deleteByProductId(Long productId);
}
