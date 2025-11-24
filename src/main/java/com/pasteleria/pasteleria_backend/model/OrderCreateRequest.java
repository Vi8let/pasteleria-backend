package com.pasteleria.pasteleria_backend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    private Double discountAmount;
    private Double discountPercentage;
    private String discountDescription;
}

