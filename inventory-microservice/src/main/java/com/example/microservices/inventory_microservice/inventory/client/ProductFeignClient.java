package com.example.microservices.inventory_microservice.inventory.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.microservices.inventory_microservice.common.response.ApiResponse;
import com.example.microservices.inventory_microservice.inventory.dto.ProductDto;

@FeignClient(name = "shop-microservice")
public interface ProductFeignClient {

    @GetMapping("/api/products/{productId}")
    ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long productId);

    @PostMapping("/api/products")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto product);
}
