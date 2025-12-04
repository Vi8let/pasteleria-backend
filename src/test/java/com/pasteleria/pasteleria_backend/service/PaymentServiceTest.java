package com.pasteleria.pasteleria_backend.service;

import com.pasteleria.pasteleria_backend.model.Order;
import com.pasteleria.pasteleria_backend.model.PaymentRequest;
import com.pasteleria.pasteleria_backend.repository.OrderRepository;
import com.pasteleria.pasteleria_backend.service.impl.MockPaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private EmailService emailService;

    private MockPaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new MockPaymentService(orderRepository, emailService);
    }

    @Test
    void processPayment_Success() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("PENDIENTE");
        order.setTotal(100.0);
        order.setUserEmail("test@example.com");

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(orderId);
        request.setCardNumber("1234567890123456");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Order result = paymentService.processPayment(request);

        // Assert
        assertEquals("PAID", result.getStatus());
        verify(orderRepository).save(order);
        verify(emailService).sendOrderConfirmation(order);
    }

    @Test
    void processPayment_OrderNotFound() {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(99L);
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> paymentService.processPayment(request));
    }

    @Test
    void processPayment_AlreadyPaid() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("PAID");

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> paymentService.processPayment(request));
    }

    @Test
    void processPayment_CardDeclined() {
        // Arrange
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("PENDIENTE");

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(orderId);
        request.setCardNumber("0000000000000000"); // Simulates decline

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> paymentService.processPayment(request));
    }
}
