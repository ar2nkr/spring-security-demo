package com.akr.services;

import com.akr.entities.CustomUserDetails;
import com.akr.entities.User;
import com.akr.repos.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    final private UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("user not found in loadUserByUsername");
        User savedUser = userRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
        return new CustomUserDetails(savedUser);
    }
}
