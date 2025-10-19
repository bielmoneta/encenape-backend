package com.encenape.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encenape.dto.EventoRequestDTO;
import com.encenape.dto.EventoResponseDTO;
import com.encenape.model.Evento;
import com.encenape.service.EventoService;

@RestController
@RequestMapping("api/eventos")
@CrossOrigin(origins = "http://localhost:3000")
public class EventoController {
    
    @Autowired
    private EventoService eventoService;

    // Criar um novo evento
    @PostMapping
    public ResponseEntity<EventoResponseDTO> criar(@RequestBody EventoRequestDTO requestDTO) {
        Evento eventoCriado = eventoService.criarEvento(requestDTO);
        return new ResponseEntity<>(new EventoResponseDTO(eventoCriado), HttpStatus.CREATED);
    }

    // LISTAR todos os eventos
    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarTodos() {
        List<Evento> eventos = eventoService.listarTodos();
        List<EventoResponseDTO> responseDTOs = eventos.stream()
                .map(EventoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // BUSCAR um evento por ID
    @GetMapping("buscar/{id}")
    public ResponseEntity<EventoResponseDTO> buscarPorId(@PathVariable Long id) {
        return eventoService.buscarPorId(id)
                .map(evento -> ResponseEntity.ok(new EventoResponseDTO(evento)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ATUALIZAR um evento
    @PutMapping("atualizar/{id}")
    public ResponseEntity<EventoResponseDTO> atualizar(@PathVariable Long id, @RequestBody EventoRequestDTO requestDTO) {
        return eventoService.atualizarEvento(id, requestDTO)
                .map(evento -> ResponseEntity.ok(new EventoResponseDTO(evento)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETAR um evento
    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (eventoService.deletarEvento(id)) {
            return ResponseEntity.noContent().build(); // Sucesso, sem conteúdo
        }
        return ResponseEntity.notFound().build(); // Não encontrado
    }
}