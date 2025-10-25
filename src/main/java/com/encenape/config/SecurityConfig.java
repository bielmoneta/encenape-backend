package com.encenape.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para simplificar testes
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/usuarios/cadastrar", 
                    "/api/usuarios/logar",
                    "/api/usuarios/esqueceu-senha",
                    "/api/usuarios/resetar-senha",
                    "/api/usuarios/{id}",
                    "/api/eventos/**"
                ).permitAll() 
                .anyRequest().authenticated()
            );
        return http.build();
    }   

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}