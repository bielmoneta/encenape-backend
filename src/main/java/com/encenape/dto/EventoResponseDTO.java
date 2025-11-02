package com.encenape.dto;

import java.time.LocalDateTime;

import com.encenape.model.Evento;
import com.encenape.model.enums.CategoriaEvento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventoResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private LocalDateTime data;
    private String local;
    private CategoriaEvento categoria;
    private String imagemUrl;

    //conversão de Model para DTO
    public EventoResponseDTO(Evento evento) {
        this.id = evento.getId();
        this.nome = evento.getNome();
        this.descricao = evento.getDescricao();
        this.data = evento.getData();
        this.local = evento.getLocal();
        this.categoria = evento.getCategoria();
        this.imagemUrl = evento.getImagemUrl();
    }
}
