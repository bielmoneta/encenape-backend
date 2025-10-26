package com.encenape.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}") // Chave secreta lida do application.properties
    private String jwtSecretString;

    @Value("${app.jwt.expiration-ms}") // Tempo de expiração lido do application.properties
    private int jwtExpirationMs;

    private SecretKey key;

    // Inicializa a chave secreta de forma segura
    @jakarta.annotation.PostConstruct
    public void init() {
        // Garante que a chave seja forte o suficiente para o algoritmo HS512
        byte[] keyBytes = jwtSecretString.getBytes();
        if (keyBytes.length < 64) { // HS512 requires at least 512 bits (64 bytes)
             throw new IllegalArgumentException("JWT secret key must be at least 64 bytes long for HS512");
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // Gera um token JWT para um usuário autenticado
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername())) // Define o "dono" do token (email)
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Data de expiração
                .signWith(key, SignatureAlgorithm.HS512) // Assina com a chave secreta
                .compact();
    }

    // Extrai o email (username) de um token JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Valida um token JWT
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT não suportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string está vazia: {}", e.getMessage());
        } catch (SignatureException e) {
             logger.error("Assinatura JWT inválida: {}", e.getMessage());
        }

        return false;
    }
}