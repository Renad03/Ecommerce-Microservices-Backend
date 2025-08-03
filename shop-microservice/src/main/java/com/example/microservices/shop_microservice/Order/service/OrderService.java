package com.example.microservices.shop_microservice.Order.service;

import com.example.microservices.shop_microservice.Cart.model.Cart;
import com.example.microservices.shop_microservice.Cart.service.CartService;
import com.example.microservices.shop_microservice.Order.model.Order;
import com.example.microservices.shop_microservice.Order.model.OrderItem;
import com.example.microservices.shop_microservice.Order.repository.OrderRepository;
import com.example.microservices.shop_microservice.Order.repository.OrderItemRepository;
import com.example.microservices.shop_microservice.Payment.service.PaymentService;
import com.example.microservices.shop_microservice.Product.model.Product;
import com.example.microservices.shop_microservice.Product.repository.ProductRepository;
import com.example.microservices.shop_microservice.Product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> findByUserId(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    @Transactional
    public Order createOrder(Long userId) {
        List<Cart> cartItems = cartService.getUserCart(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot create order: cart is empty.");
        }

        double total = 0;
        for (Cart item : cartItems) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + item.getProductId()));

            // Check if enough stock is available
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }

            total += product.getPrice() * item.getQuantity();
        }

        // Save the order with status
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order = orderRepo.save(order);

        // Create and save order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        // Link order items to order and save again (optional)
        order.setItems(orderItems);
        order = orderRepo.save(order);

        // Reduce product stock
        for (Cart item : cartItems) {
            productService.reduceStock(item.getProductId(), item.getQuantity());
        }

        // Process payment
        paymentService.processPaymentSafe(userId, order.getId());
            
        // Clear cart
        cartService.clearCart(userId);

        return order;
    }

 
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (order.getStatus().equalsIgnoreCase("CANCELLED")) {
            throw new IllegalStateException("Order is already cancelled.");
        }

        // Restore stock for each item
        List<OrderItem> items = order.getItems(); // assuming you have a getItems() method

        for (OrderItem item : items) {
            productService.increaseStock(item.getProductId(), item.getQuantity());
        }

        //refund payment
        paymentService.refund(orderId);

        // Update order status
        order.setStatus("CANCELLED");
        return orderRepo.save(order);
    }

    public Order getOrderById(Long orderId) {  
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        return orderItemRepository.findByOrder(order);
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setStatus(status);
        return orderRepo.save(order);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        orderRepo.delete(order);
    }
}
