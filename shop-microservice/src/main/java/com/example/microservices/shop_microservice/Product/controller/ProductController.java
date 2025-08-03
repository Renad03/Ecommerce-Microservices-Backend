package com.example.microservices.shop_microservice.Product.controller;

import com.example.microservices.shop_microservice.common.response.ApiResponse;
import com.example.microservices.shop_microservice.Product.model.Product;
import com.example.microservices.shop_microservice.Product.service.ProductService;
import com.example.microservices.shop_microservice.Product.dto.UpdateProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody Product p) {
        Product product = service.addProduct(p);
        return ResponseEntity.status(201).body(
                new ApiResponse("Product is added successfully", 201, product)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<Product> productPage = service.getAllProducts(page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("products", productPage.getContent());
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());

            return ResponseEntity.status(200).body(
                    new ApiResponse("Products fetched successfully", 200, response));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(
                    new ApiResponse(ex.getMessage(), 404, null));
        }
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        Optional<Product> product = service.getProductById(productId);
                if(product.isPresent()){
                    return ResponseEntity.status(200).body(new ApiResponse(
                            "Product with id: " + productId + " is fetched successfully",
                            200,
                            product.get()
                    ));
                }
                else {
                    return ResponseEntity.status(404).body(new ApiResponse(
                            "Can't find the product with id: " + productId,
                            404,
                            product
                    ));
                }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductDto dto) {
        try {
            Product updatedProduct = service.updateProductFields(productId, dto);
            return ResponseEntity.status(201).body(
                    new ApiResponse("Product with id: " + productId + " is updated successfully",
                            201,
                            updatedProduct));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(
                    new ApiResponse(ex.getMessage(), 404, null));
        }
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        boolean deleted = service.deleteProduct(productId);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse("Product is deleted successfully", 200, null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse("Product is not found", 404, null));
        }
    }
}
