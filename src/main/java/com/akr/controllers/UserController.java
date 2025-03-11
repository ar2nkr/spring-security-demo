package com.akr.controllers;

import com.akr.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private List<User> users = new ArrayList<>(
            List.of(
            new User("user1", "password1"),
            new User("user2", "password2"),
            new User("user3", "password3")
            )
    );


    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }


    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

}
