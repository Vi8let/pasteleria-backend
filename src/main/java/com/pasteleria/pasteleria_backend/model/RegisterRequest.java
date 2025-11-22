package com.pasteleria.pasteleria_backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank
    private String fullName;

    private String run;
    private String fechaNacimiento;
    private String region;
    private String comuna;
    private String direccion;
    private String codigoPromocion;
}

