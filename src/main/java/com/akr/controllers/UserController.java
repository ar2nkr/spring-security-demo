package com.akr.controllers;

import com.akr.dtos.LoginRequestDTO;
import com.akr.dtos.UserRegistrationDTO;
import com.akr.entities.User;
import com.akr.repos.UserRepo;
import com.akr.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User user = userService.registerNewUser(registrationDTO);
            return ResponseEntity.ok("User registered successfully: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        return new ResponseEntity<>(userService.verify(loginRequest), HttpStatus.OK);
    }

}
