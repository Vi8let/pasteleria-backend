package com.pasteleria.pasteleria_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String role;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @Column
    private String run;

    @Column
    private String fechaNacimiento;

    @Column
    private String region;

    @Column
    private String comuna;

    @Column
    private String direccion;

    @Column
    private String codigoDescuento;
}