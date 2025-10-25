package com.encenape.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetarSenhaDTO {
    private String token;
    private String novaSenha;
}