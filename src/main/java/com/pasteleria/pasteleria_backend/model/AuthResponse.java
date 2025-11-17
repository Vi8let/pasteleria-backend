package com.pasteleria.pasteleria_backend.model;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean ok;
    private String token;
    private String role;
    private String email;
}
