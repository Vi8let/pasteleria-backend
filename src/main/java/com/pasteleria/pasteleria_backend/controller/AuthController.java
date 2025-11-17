package com.pasteleria.pasteleria_backend.controller;

import com.pasteleria.pasteleria_backend.model.User;
import com.pasteleria.pasteleria_backend.repository.UserRepository;
import com.pasteleria.pasteleria_backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User u) {
        // verifica si email ya existe
        if (userRepository.findByEmail(u.getEmail()) != null) {
            return Map.of("ok", false, "message", "Email already taken");
        }
        u.setRole("USER");
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        User saved = userRepository.save(u);
        return Map.of("ok", true, "userId", saved.getId());
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        // Autentica con AuthenticationManager (lanzará excepción si falla)
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // si ok, buscamos user y generamos token
        User u = userRepository.findByEmail(email);
        String token = jwtUtil.generateToken(u.getEmail(), u.getRole());

        return Map.of("ok", true, "token", token, "role", u.getRole(), "email", u.getEmail());
    }
}
