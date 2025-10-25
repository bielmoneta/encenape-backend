package com.encenape.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.encenape.model.enums.CategoriaEvento;
import jakarta.persistence.EnumType; 
import jakarta.persistence.Enumerated;

@Entity
@Table(name = "eventos")
@Getter
@Setter
public class Evento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    //columnDefinition = "TEXT" para permitir descrições longas.
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false, length = 200)
    private String local;

    @Enumerated(EnumType.STRING) // Diz ao JPA para salvar o NOME da categoria (ex: "TEATRO") no banco
    @Column(nullable = false)
    private CategoriaEvento categoria;
}
