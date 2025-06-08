package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public CartController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCartFromSession(session);
        List<Map<String, Object>> cartItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            
            Product product = productService.findById(productId).orElse(null);
            if (product != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("product", product);
                item.put("quantity", quantity);
                
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                item.put("subtotal", itemTotal);
                
                cartItems.add(item);
                total = total.add(itemTotal);
            }
        }
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") Integer quantity, 
                           HttpSession session) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        if (!product.isInStock() || product.getStockQuantity() < quantity) {
            return "redirect:/products/" + productId + "?error=outofstock";
        }
        
        Map<Long, Integer> cart = getCartFromSession(session);
        
        // Update quantity if product already in cart
        if (cart.containsKey(productId)) {
            cart.put(productId, cart.get(productId) + quantity);
        } else {
            cart.put(productId, quantity);
        }
        
        session.setAttribute("cart", cart);
        
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Map<String, String> formData, HttpSession session) {
        Map<Long, Integer> cart = getCartFromSession(session);
        
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            if (entry.getKey().startsWith("quantity_")) {
                Long productId = Long.parseLong(entry.getKey().substring(9));
                Integer quantity = Integer.parseInt(entry.getValue());
                
                if (quantity <= 0) {
                    cart.remove(productId);
                } else {
                    Product product = productService.findById(productId).orElse(null);
                    if (product != null && product.getStockQuantity() >= quantity) {
                        cart.put(productId, quantity);
                    }
                }
            }
        }
        
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, HttpSession session) {
        Map<Long, Integer> cart = getCartFromSession(session);
        cart.remove(productId);
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Map<Long, Integer> cart = getCartFromSession(session);
        if (cart.isEmpty()) {
            return "redirect:/cart?error=empty";
        }
        
        List<Map<String, Object>> cartItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            
            Product product = productService.findById(productId).orElse(null);
            if (product != null) {
                // Check if product is still in stock
                if (!product.isInStock() || product.getStockQuantity() < quantity) {
                    return "redirect:/cart?error=stockchanged";
                }
                
                Map<String, Object> item = new HashMap<>();
                item.put("product", product);
                item.put("quantity", quantity);
                
                BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                item.put("subtotal", itemTotal);
                
                cartItems.add(item);
                total = total.add(itemTotal);
            }
        }
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        
        return "cart/checkout";
    }

    private Map<Long, Integer> getCartFromSession(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        
        return cart;
    }
}
