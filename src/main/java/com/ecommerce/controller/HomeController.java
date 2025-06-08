package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Product> latestProducts = productService.findLatestProducts();
        List<Product> topSellingProducts = productService.findTopSellingProducts();
        
        model.addAttribute("latestProducts", latestProducts);
        model.addAttribute("topSellingProducts", topSellingProducts);
        
        return "home";
    }

    // Login mapping removed to avoid conflict with LoginController

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
