package com.example.microservices.wallet_microservice.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.microservices.wallet_microservice.common.response.ApiResponse;
import com.example.microservices.wallet_microservice.user.dto.UpdateUserDto;
import com.example.microservices.wallet_microservice.user.model.User;
import com.example.microservices.wallet_microservice.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse("User registered successfully", 200, savedUser));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(new ApiResponse("User found", 200, user.get()));
        } else {
            return ResponseEntity.status(404)
                    .body(new ApiResponse("User with username '" + username + "' not found", 404, null));
        }
    }


    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse("User list fetched", 200, users));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id,
                                                  @RequestBody UpdateUserDto updateDto,
                                                  HttpServletRequest request) {
        User updated = userService.updateUser(id, updateDto);
        if (updated != null) {
            return ResponseEntity.ok(new ApiResponse("User updated successfully", 200, updated));
        } else {
            return ResponseEntity.status(404)
                    .body(new ApiResponse("User with ID " + id + " not found", 404, null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", 200, null));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse("User not found", 404, null));
        }
    }

}
