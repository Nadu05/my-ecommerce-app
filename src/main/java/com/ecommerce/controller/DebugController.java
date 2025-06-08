package com.ecommerce.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/debug/auth")
    public Map<String, Object> debugAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> result = new HashMap<>();
        
        result.put("isAuthenticated", auth.isAuthenticated());
        result.put("principal", auth.getPrincipal().toString());
        result.put("name", auth.getName());
        result.put("authorities", auth.getAuthorities());
        
        return result;
    }
}
