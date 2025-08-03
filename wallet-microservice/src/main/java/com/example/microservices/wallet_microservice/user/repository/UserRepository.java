package com.example.microservices.wallet_microservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservices.wallet_microservice.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByUsername(String username);
}
