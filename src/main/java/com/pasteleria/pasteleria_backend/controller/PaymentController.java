package com.pasteleria.pasteleria_backend.controller;

import com.pasteleria.pasteleria_backend.model.Order;
import com.pasteleria.pasteleria_backend.model.PaymentRequest;
import com.pasteleria.pasteleria_backend.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Order> createPayment(@Valid @RequestBody PaymentRequest request) {
        Order paidOrder = paymentService.processPayment(request);
        return ResponseEntity.ok(paidOrder);
    }
}
