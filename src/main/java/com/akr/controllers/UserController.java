package com.akr.controllers;

import com.akr.dtos.AuthResponse;
import com.akr.dtos.LoginRequest;
import com.akr.dtos.RegisterRequest;
import com.akr.entities.User;
import com.akr.repos.UserRepo;
import com.akr.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    final private UserRepo userRepo;
    final private UserService userService;

    public UserController(UserRepo userRepo, UserService userService){
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin";
    }
    @GetMapping("/user")
    public String user() {
        return "Hello User";
    }
    @GetMapping("/user/all")
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

}
