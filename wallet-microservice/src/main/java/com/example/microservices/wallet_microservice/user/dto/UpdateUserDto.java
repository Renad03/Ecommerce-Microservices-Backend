package com.example.microservices.wallet_microservice.user.dto;

public class UpdateUserDto {
    private String username;
    private String email;
    private String password;


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
