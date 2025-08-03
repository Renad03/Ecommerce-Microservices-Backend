package com.example.microservices.inventory_microservice.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservices.inventory_microservice.common.response.ApiResponse;
import com.example.microservices.inventory_microservice.inventory.dto.ProductDto;
import com.example.microservices.inventory_microservice.inventory.model.Inventory;
import com.example.microservices.inventory_microservice.inventory.service.InventoryService;

@RestController
@RequestMapping("api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @PostMapping("/add-stock")
    public ResponseEntity<ApiResponse> addStock(@RequestBody Inventory inventory) {
        Inventory saved = inventoryService.addStock(inventory);
    return ResponseEntity.status(200).body(new ApiResponse("Stock added successfully", 200, null));
    }

    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto product) {
        inventoryService.addProduct(product);
        ApiResponse response = new ApiResponse("Product added successfully", 200, null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-inventory/{inventoryId}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long inventoryId) {
        try{
            inventoryService.deleteById(inventoryId);
            ApiResponse response = new ApiResponse("Inventory deleted successfully", 200, null);
            return ResponseEntity.ok(response);
        }catch(RuntimeException ex){
            ApiResponse response = new ApiResponse("Error deleting inventory: " + ex.getMessage(), 500, null);
            return ResponseEntity.status(500).body(response);
        }
        
    }
    
    @GetMapping("/get-all-inventories")
    public ResponseEntity<ApiResponse> getAllInventories() {
        var inventories = inventoryService.getAllInventories();
        ApiResponse response = new ApiResponse("Inventories found", 200, inventories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-inventory-by-id/{id}")
    public ResponseEntity<ApiResponse> getInventoryById(@PathVariable Long id) {
        Inventory inventoryOpt = inventoryService.getInventoryById(id);
        if (inventoryOpt.getId() != null) {
            ApiResponse response = new ApiResponse("Inventory found", 200, inventoryOpt);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse response = new ApiResponse("Inventory not found", 404, null);
            return ResponseEntity.status(404).body(response);
        }
    }
}
