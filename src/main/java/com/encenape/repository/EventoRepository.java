package com.encenape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.encenape.model.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>{
    
}