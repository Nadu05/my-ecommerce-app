package com.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime orderDate;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(nullable = false)
    private BigDecimal total;
    
    private String shippingAddress;
    
    private String paymentMethod;
    
    private String trackingNumber;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    public enum Status {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
    
    public void addOrderItem(Product product, int quantity, BigDecimal price) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(this);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);
        orderItems.add(orderItem);
    }
}
