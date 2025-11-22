package com.pasteleria.pasteleria_backend.controller;

import com.pasteleria.pasteleria_backend.model.User;
import com.pasteleria.pasteleria_backend.model.UserProfile;
import com.pasteleria.pasteleria_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfile> getAll() {
        return userRepository.findAll().stream()
                .map(this::toUserProfile)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfile getById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return toUserProfile(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        // No permitir eliminar al propio admin
        User currentUser = userRepository.findByEmail(authentication.getName());
        if (currentUser != null && currentUser.getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes eliminar tu propia cuenta");
        }
        
        // No permitir eliminar otros admins
        if ("ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden eliminar usuarios administradores");
        }
        
        userRepository.delete(user);
    }

    @PatchMapping("/{id}/promotion-code")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfile updatePromotionCode(@PathVariable Long id, @RequestBody PromotionCodeRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        user.setCodigoDescuento(request.getCodigoPromocion());
        userRepository.save(user);
        
        return toUserProfile(user);
    }

    private UserProfile toUserProfile(User user) {
        UserProfile profile = new UserProfile();
        profile.setId(user.getId());
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

    // Clase interna para el request
    public static class PromotionCodeRequest {
        private String codigoPromocion;

        public String getCodigoPromocion() {
            return codigoPromocion;
        }

        public void setCodigoPromocion(String codigoPromocion) {
            this.codigoPromocion = codigoPromocion;
        }
    }
}

