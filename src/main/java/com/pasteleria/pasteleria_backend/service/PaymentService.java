package com.pasteleria.pasteleria_backend.service;

import com.pasteleria.pasteleria_backend.model.Order;
import com.pasteleria.pasteleria_backend.model.PaymentRequest;

public interface PaymentService {
    Order processPayment(PaymentRequest paymentRequest);
}
