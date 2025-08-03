package com.example.microservices.shop_microservice.Product.repository;

import com.example.microservices.shop_microservice.Product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
