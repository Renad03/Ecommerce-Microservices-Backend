package com.example.microservices.shop_microservice.Order.repository;

import java.util.List;

import com.example.microservices.shop_microservice.Order.model.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.shop_microservice.Order.model.Order;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    public List<OrderItem> findByOrder(Order order);
}
