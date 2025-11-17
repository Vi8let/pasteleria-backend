package com.pasteleria.pasteleria_backend.repository;

import com.pasteleria.pasteleria_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
