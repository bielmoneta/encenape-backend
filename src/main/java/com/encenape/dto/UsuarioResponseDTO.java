package com.encenape.dto;

import com.encenape.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDTO {
    //apenas os dados que o usuario pode ver
    private Long id;
    private String nome;
    private String email;
    
    //converte o model para dto 
    public UsuarioResponseDTO(Usuario usuario){
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }
}
