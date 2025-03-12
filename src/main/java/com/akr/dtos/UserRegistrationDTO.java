package com.akr.dtos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {
    private String username;
    private String password;
    private String role; // Accepts "ROLE_USER" or "ROLE_ADMIN"
}

