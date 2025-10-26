package com.encenape.dto; // Garanta que o package está correto

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse { // Adicione 'public'
    private String token;
    private Long id;
    private String nome;
    private String email;
}