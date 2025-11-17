package com.pasteleria.pasteleria_backend.config;

import com.pasteleria.pasteleria_backend.model.User;
import com.pasteleria.pasteleria_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@pasteleria.com") == null) {
                User admin = new User();
                admin.setEmail("admin@pasteleria.com");
                admin.setFullName("Administrador General");
                admin.setRole("ADMIN");
                admin.setPassword(passwordEncoder.encode("Admin123*"));
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("cliente@pasteleria.com") == null) {
                User user = new User();
                user.setEmail("cliente@pasteleria.com");
                user.setFullName("Cliente Oficial");
                user.setRole("USER");
                user.setPassword(passwordEncoder.encode("Cliente123*"));
                userRepository.save(user);
            }
        };
    }
}

