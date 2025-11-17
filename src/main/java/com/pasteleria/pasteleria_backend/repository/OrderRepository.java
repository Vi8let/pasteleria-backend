package com.pasteleria.pasteleria_backend.repository;

import com.pasteleria.pasteleria_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
