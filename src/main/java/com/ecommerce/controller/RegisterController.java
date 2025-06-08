package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("usernameError", "Username already exists");
            return "register";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("emailError", "Email already registered");
            return "register";
        }

        userService.save(user);
        return "redirect:/login?registered";
    }
}