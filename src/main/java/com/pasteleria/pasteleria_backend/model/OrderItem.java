package com.pasteleria.pasteleria_backend.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Embeddable
@Data
public class OrderItem {

    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    @NotNull
    private Double unitPrice;

    @NotNull
    @Min(1)
    private Integer quantity;
}

