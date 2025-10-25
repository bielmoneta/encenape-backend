package com.encenape.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.encenape.model.Evento;
import com.encenape.model.enums.CategoriaEvento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{
    
    List<Evento> findByCategoria(CategoriaEvento categoria);
}