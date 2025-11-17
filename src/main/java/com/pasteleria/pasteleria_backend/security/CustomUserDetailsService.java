package com.pasteleria.pasteleria_backend.security;

import com.pasteleria.pasteleria_backend.model.User;
import com.pasteleria.pasteleria_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository repo) {
        this.userRepository = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByEmail(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // Creamos UserDetails usando EL User de Spring, no tu entidad
        return org.springframework.security.core.userdetails.User
                .withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .roles(appUser.getRole()) // Spring añade ROLE_ automáticamente
                .build();
    }
}
