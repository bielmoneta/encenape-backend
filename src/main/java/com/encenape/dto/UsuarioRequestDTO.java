package com.encenape.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequestDTO {
    //apenas os dados que o usuario precisa enviar
    private String nome;
    private String email;
    private String senha;  

}
