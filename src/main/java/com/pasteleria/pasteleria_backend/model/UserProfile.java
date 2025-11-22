package com.pasteleria.pasteleria_backend.model;

import lombok.Data;

@Data
public class UserProfile {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String run;
    private String fechaNacimiento;
    private String region;
    private String comuna;
    private String direccion;
    private String codigoDescuento;
}

