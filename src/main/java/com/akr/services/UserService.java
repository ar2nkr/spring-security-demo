package com.akr.services;

import com.akr.dtos.LoginRequestDTO;
import com.akr.dtos.UserRegistrationDTO;
import com.akr.entities.Role;
import com.akr.entities.User;
import com.akr.repos.RoleRepo;
import com.akr.repos.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        if (userRepo.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registrationDTO.getPassword()));
        user.setEnabled(true);

        // Assigning Role
        Role role = roleRepo.findByName(registrationDTO.getRole());
        if (role == null) {
            throw new RuntimeException("Invalid role: " + registrationDTO.getRole());
        }

        user.setRoles(Collections.singleton(role));

        return userRepo.save(user);
    }

    public String verify(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );
//        return authentication.isAuthenticated() ? user : null;
//        return userRepo.findById(user.getId()).orElseThrow(()-> new UsernameNotFoundException("user not found"));
        if(!authentication.isAuthenticated()) throw new UsernameNotFoundException("no user found");
        return jwtService.generateToken(loginRequestDTO.getUsername());
    }
}
