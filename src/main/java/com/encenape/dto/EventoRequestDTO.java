package com.encenape.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventoRequestDTO {
    private String nome;
    private String descricao;
    private LocalDateTime data;
    private String local;
}
