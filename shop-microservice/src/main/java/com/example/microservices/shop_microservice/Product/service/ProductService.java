package com.example.microservices.shop_microservice.Product.service;

import com.example.microservices.shop_microservice.Product.dto.UpdateProductDto;
import com.example.microservices.shop_microservice.Product.model.Product;
import com.example.microservices.shop_microservice.Product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepo;

    @Transactional
    public Product addProduct(Product p) {
        return productRepo.save(p);
    }

    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepo.findAll(pageable);

        if (productPage.isEmpty()) {
            throw new RuntimeException("No products found.");
        }

        return productPage;
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepo.findById(productId);
    }

    @Transactional
    public Product updateProductFields(Long productId, UpdateProductDto dto) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (dto.getName() != null) {
            existingProduct.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            existingProduct.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            existingProduct.setPrice(dto.getPrice());
        }
        if (dto.getStock() != null) {
            existingProduct.setStock(dto.getStock());
        }

        return productRepo.save(existingProduct);
    }

    @Transactional
    public boolean deleteProduct(Long productId) {
        Optional<Product> product = productRepo.findById(productId);
        if (product.isPresent()) {
            productRepo.deleteById(productId);
            return true;
        }
        else{
           return false;
        }

    }

    @Transactional
    public void reduceStock(Long productId, int quantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product ID: " + productId);
        }

        product.setStock(product.getStock() - quantity);
        productRepo.save(product);
    }

    @Transactional
    public void increaseStock(Long productId, int quantity) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        product.setStock(product.getStock() + quantity);
        productRepo.save(product);
    }
}

