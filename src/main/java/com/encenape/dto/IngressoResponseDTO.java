package com.encenape.dto;

import com.encenape.model.Ingresso;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class IngressoResponseDTO {

    private Long id;
    private LocalDateTime dataCompra;
    private Long eventoId;
    private String eventoNome;
    private LocalDateTime eventoData;
    private String eventoLocal;
    //lista:adicionar status, qr code, etc.

    // Construtor que pega o Ingresso e extrai informações do Evento associado
    public IngressoResponseDTO(Ingresso ingresso) {
        this.id = ingresso.getId();
        this.dataCompra = ingresso.getDataCompra();
        if (ingresso.getEvento() != null) {
            this.eventoId = ingresso.getEvento().getId();
            this.eventoNome = ingresso.getEvento().getNome();
            this.eventoData = ingresso.getEvento().getData();
            this.eventoLocal = ingresso.getEvento().getLocal();
        }
    }
}