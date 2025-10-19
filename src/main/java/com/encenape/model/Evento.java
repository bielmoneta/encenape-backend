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
}
