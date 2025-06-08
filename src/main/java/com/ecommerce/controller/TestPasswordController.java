package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestPasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/debug/password")
    public Map<String, Object> testPassword(@RequestParam String rawPassword) {
        Map<String, Object> result = new HashMap<>();
        
        // Encode the provided password
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        // Check if it matches the admin password from data.sql
        String adminPasswordFromDb = "$2a$10$3JUzZVzYh8EKF5eFU.ixVeV2HS6xZKMoK1h7xg.Wz.p1VuUNncvYi";
        boolean matchesAdmin = passwordEncoder.matches(rawPassword, adminPasswordFromDb);
        
        // Check if it matches the user password from data.sql
        String userPasswordFromDb = "$2a$10$xLiUyWBrIhZ5Vv9P89gOA.1Z7k.vwLj6Tw5gPmQGMPi5jOV9Rhnk2";
        boolean matchesUser = passwordEncoder.matches(rawPassword, userPasswordFromDb);
        
        result.put("rawPassword", rawPassword);
        result.put("encodedPassword", encodedPassword);
        result.put("matchesAdminPassword", matchesAdmin);
        result.put("matchesUserPassword", matchesUser);
        
        return result;
    }
}
