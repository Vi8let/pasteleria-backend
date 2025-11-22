package com.pasteleria.pasteleria_backend.controller;

import com.pasteleria.pasteleria_backend.model.*;
import com.pasteleria.pasteleria_backend.repository.UserRepository;
import com.pasteleria.pasteleria_backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRun(request.getRun());
        user.setFechaNacimiento(request.getFechaNacimiento());
        user.setRegion(request.getRegion());
        user.setComuna(request.getComuna());
        user.setDireccion(request.getDireccion());
        user.setCodigoDescuento(request.getCodigoPromocion());

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }

        return buildAuthResponse(user);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserProfile me(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }

        UserProfile profile = new UserProfile();
        profile.setEmail(user.getEmail());
        profile.setFullName(user.getFullName());
        profile.setRole(user.getRole());
        profile.setRun(user.getRun());
        profile.setFechaNacimiento(user.getFechaNacimiento());
        profile.setRegion(user.getRegion());
        profile.setComuna(user.getComuna());
        profile.setDireccion(user.getDireccion());
        profile.setCodigoDescuento(user.getCodigoDescuento());
        return profile;
    }

    private AuthResponse buildAuthResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setOk(true);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setToken(jwtUtil.generateToken(user.getEmail(), user.getRole()));
        return response;
    }
}
