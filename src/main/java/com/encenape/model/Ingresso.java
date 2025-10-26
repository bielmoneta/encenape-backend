package com.encenape.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingressos")
@Getter
@Setter
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento Muitos-para-Um: Muitos ingressos pertencem a UM Usuário
    @ManyToOne(fetch = FetchType.LAZY) // LAZY = Não carrega os dados do usuário a menos que seja pedido
    @JoinColumn(name = "usuario_id", nullable = false) // Nome da coluna FK no banco
    private Usuario usuario;

    // Relacionamento Muitos-para-Um: Muitos ingressos pertencem a UM Evento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    //lista:
    // adicionar outros campos no futuro: private String codigoQr;
    // @Enumerated(EnumType.STRING)
    // private StatusIngresso status; // Ex: VALIDO, UTILIZADO, CANCELADO.
}