package com.encenape.config;

import com.encenape.security.JwtAuthFilter;
import com.encenape.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    // Bean para o codificador de senha
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para o gerenciador de autenticação (necessário para o endpoint de login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // A configuração principal da cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF (comum em APIs stateless)
            .csrf(csrf -> csrf.disable()) 
            
            // Configura o gerenciamento de sessão como STATELESS (sem sessão no servidor)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            
            // Configura as regras de autorização para as requisições HTTP
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // Endpoints públicos (não exigem autenticação)
                .requestMatchers(
                    "/api/usuarios/cadastrar", 
                    "/api/usuarios/logar",
                    "/api/usuarios/esqueceu-senha",
                    "/api/usuarios/resetar-senha",
                    "/api/eventos",             
                    "/api/eventos/categoria/**" 
                ).permitAll()

                .requestMatchers("/api/ingressos/**").authenticated()
                
                // Qualquer outra requisição exige autenticação
                .anyRequest().authenticated() 
            )
            
            // Adiciona nosso filtro JWT ANTES do filtro padrão de autenticação por usuário/senha
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); 

        return http.build();
    }
}