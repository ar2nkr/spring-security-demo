package com.akr.services;

import com.akr.dtos.AuthResponse;
import com.akr.dtos.LoginRequest;
import com.akr.dtos.RegisterRequest;
import com.akr.entities.Role;
import com.akr.entities.User;
import com.akr.repos.RoleRepo;
import com.akr.repos.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    public ResponseEntity<AuthResponse> registerUser(RegisterRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Error: Username is already taken!"));
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

        // Assign roles
        Set<Role> roles = new HashSet<>();
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role defaultRole = roleRepo.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));
            roles.add(defaultRole);
        } else {
            roles = request.getRoles().stream()
                    .map(roleName -> roleRepo.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " not found")))
                    .collect(Collectors.toSet());
        }

        user.setRoles(roles);
        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("User registered successfully!"));
    }

    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(new AuthResponse("Login successful!", jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse("Invalid username or password!"));
        }
    }
}
