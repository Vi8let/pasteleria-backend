package com.pasteleria.pasteleria_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Double total;
    private String status;
}
