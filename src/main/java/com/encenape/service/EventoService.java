package com.encenape.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.encenape.dto.EventoRequestDTO;
import com.encenape.model.Evento;
import com.encenape.repository.EventoRepository;

@Service
public class EventoService {
    
    @Autowired
    private EventoRepository eventoRepository;

    public Evento criarEvento(EventoRequestDTO requestDTO) {
        Evento novoEvento = new Evento();
        novoEvento.setNome(requestDTO.getNome());
        novoEvento.setDescricao(requestDTO.getDescricao());
        novoEvento.setData(requestDTO.getData());
        novoEvento.setLocal(requestDTO.getLocal());
        return eventoRepository.save(novoEvento);
    }

    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> buscarPorId(Long id) {
        return eventoRepository.findById(id);
    }

    public Optional<Evento> atualizarEvento(Long id, EventoRequestDTO requestDTO) {
        return eventoRepository.findById(id).map(eventoExistente -> {
            eventoExistente.setNome(requestDTO.getNome());
            eventoExistente.setDescricao(requestDTO.getDescricao());
            eventoExistente.setData(requestDTO.getData());
            eventoExistente.setLocal(requestDTO.getLocal());
            return eventoRepository.save(eventoExistente);
        });
    }

    public boolean deletarEvento(Long id) {
        if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
