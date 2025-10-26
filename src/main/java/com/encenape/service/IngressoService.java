package com.encenape.service;

import com.encenape.model.Evento;
import com.encenape.model.Ingresso;
import com.encenape.model.Usuario;
import com.encenape.repository.EventoRepository;
import com.encenape.repository.IngressoRepository;
import com.encenape.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para garantir consistência

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IngressoService {

    @Autowired
    private IngressoRepository ingressoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; 

    @Autowired
    private EventoRepository eventoRepository;  

    @Transactional // Garante que todas as operações com o banco ocorram ou nenhuma ocorra
    public Ingresso comprarIngresso(Long usuarioId, Long eventoId) {
        // 1. Busca o usuário 
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        // 2. Busca o evento
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + eventoId));

        // TODO: Adicionar lógica futura (ex: verificar se o evento já passou, se há limite de ingressos, etc.)

        // 3. Cria o novo ingresso
        Ingresso novoIngresso = new Ingresso();
        novoIngresso.setUsuario(usuario);
        novoIngresso.setEvento(evento);
        novoIngresso.setDataCompra(LocalDateTime.now());
        // novoIngresso.setStatus(StatusIngresso.VALIDO); // Exemplo futuro

        // 4. Salva o ingresso no banco
        return ingressoRepository.save(novoIngresso);
    }

    @Transactional(readOnly = true) // Operação de leitura
    public List<Ingresso> buscarIngressosPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + usuarioId);
        }
        return ingressoRepository.findByUsuarioId(usuarioId);
    }
}
