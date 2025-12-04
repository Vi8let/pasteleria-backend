package com.pasteleria.pasteleria_backend.service.impl;

import com.pasteleria.pasteleria_backend.model.Order;
import com.pasteleria.pasteleria_backend.model.PaymentRequest;
import com.pasteleria.pasteleria_backend.repository.OrderRepository;
import com.pasteleria.pasteleria_backend.service.PaymentService;
import com.pasteleria.pasteleria_backend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MockPaymentService implements PaymentService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;

    public MockPaymentService(OrderRepository orderRepository, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public Order processPayment(PaymentRequest paymentRequest) {
        // 1. Validar que la orden exista
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));

        // 2. Validar que no esté ya pagada
        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La orden ya ha sido pagada");
        }

        // 3. Simular validación de tarjeta (Mock: Aceptamos todo lo que no sea "0000")
        if ("0000000000000000".equals(paymentRequest.getCardNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tarjeta rechazada por el banco (Simulación)");
        }

        // 4. Actualizar estado
        order.setStatus("PAID");
        Order savedOrder = orderRepository.save(order);

        // 5. Enviar correo (Async o directo)
        try {
            emailService.sendOrderConfirmation(savedOrder);
        } catch (Exception e) {
            // Loguear error pero no fallar el pago
            System.err.println("Error enviando correo de confirmación: " + e.getMessage());
        }

        return savedOrder;
    }
}
