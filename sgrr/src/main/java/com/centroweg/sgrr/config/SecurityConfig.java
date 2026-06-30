package com.centroweg.sgrr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Disponibiliza o PasswordEncoder usado para nao guardar a senha do
 * usuario em texto puro no banco. Aqui usamos so a lib de criptografia
 * (spring-security-crypto), sem ativar o Spring Security completo, entao
 * os endpoints continuam liberados como ja estavam.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
