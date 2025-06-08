package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public String viewOrders(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<Order> orders = orderService.findOrdersByUser(user);
        model.addAttribute("orders", orders);
        
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Order order = orderService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Ensure the order belongs to the current user or the user is an admin
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            return "redirect:/orders?error=unauthorized";
        }
        
        model.addAttribute("order", order);
        
        return "orders/details";
    }

    @PostMapping("/create")
    public String createOrder(Principal principal, HttpSession session, Model model) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Check if user has address
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            return "redirect:/cart/checkout?error=noaddress";
        }
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart?error=empty";
        }
        
        // Create a new order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        
        // Calculate total and add order items
        BigDecimal total = BigDecimal.ZERO;
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            
            Product product = productService.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            
            // Check if product is still in stock
            if (!product.isInStock() || product.getStockQuantity() < quantity) {
                return "redirect:/cart?error=stockchanged";
            }
            
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            total = total.add(itemTotal);
            
            // Add order item
            order.addOrderItem(product, quantity, product.getPrice());
            
            // Update product stock
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productService.saveProduct(product);
        }
        
        // Add shipping cost
        BigDecimal shippingCost = new BigDecimal("5.99");
        total = total.add(shippingCost);
        
        // Add tax (8%)
        BigDecimal tax = total.multiply(new BigDecimal("0.08"));
        total = total.add(tax);
        
        order.setTotal(total);
        
        // Save the order
        orderService.saveOrder(order);
        
        // Clear the cart
        session.removeAttribute("cart");
        
        return "redirect:/orders/" + order.getId() + "?success=created";
    }
}
