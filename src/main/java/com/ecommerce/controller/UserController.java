package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

   
    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@Valid @ModelAttribute("user") User user, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            return "user/edit-profile";
        }

        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update only allowed fields (not username, password, or role)
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setAddress(user.getAddress());
        currentUser.setPhoneNumber(user.getPhoneNumber());

        userService.updateUser(currentUser);
        return "redirect:/profile?updated";
    }

    @PostMapping("/profile/update-address")
    public String updateAddress(@RequestParam("address") String address,
            @RequestParam("phoneNumber") String phoneNumber,
            Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        currentUser.setAddress(address);
        currentUser.setPhoneNumber(phoneNumber);

        userService.updateUser(currentUser);

        // Check if the request came from the checkout page
        String referer = "cart/checkout";
        if (referer != null && referer.contains("checkout")) {
            return "redirect:/cart/checkout";
        }

        return "redirect:/profile?updated";
    }
}
