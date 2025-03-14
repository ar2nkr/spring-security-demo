package com.akr.dtos;

public class AuthResponse {
    private String message;
    private String token; // Optional JWT token

    // Constructor for messages (e.g., during registration)
    public AuthResponse(String message) {
        this.message = message;
    }

    // Constructor for login response (e.g., returning JWT)
    public AuthResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}