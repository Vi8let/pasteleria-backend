package com.pasteleria.pasteleria_backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long orderId;

    @NotBlank(message = "El número de tarjeta es obligatorio")
    private String cardNumber;

    @NotBlank(message = "El titular de la tarjeta es obligatorio")
    private String cardHolder;

    @NotBlank(message = "La fecha de expiración es obligatoria (MM/YY)")
    private String expirationDate;

    @NotBlank(message = "El CVV es obligatorio")
    private String cvv;
}
