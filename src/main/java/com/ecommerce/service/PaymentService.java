package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.Payment;
import com.ecommerce.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Order order, Payment.PaymentMethod paymentMethod, String transactionId) {
        Payment payment = new Payment();
        payment.setAmount(order.getTotal());
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionId(transactionId);
        
        return paymentRepository.save(payment);
    }

    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment updatePaymentStatus(Long paymentId, Payment.PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}
