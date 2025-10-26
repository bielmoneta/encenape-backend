package com.encenape.controller;

import com.encenape.dto.IngressoResponseDTO;
import com.encenape.model.Ingresso;
import com.encenape.security.UserDetailsImpl; // Importe UserDetailsImpl
import com.encenape.service.IngressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Importe
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingressos")
@CrossOrigin(origins = "http://localhost:4200")
public class IngressoController {

    @Autowired
    private IngressoService ingressoService;

    @PostMapping("/comprar/{eventoId}")
    public ResponseEntity<?> comprarIngresso(
            @PathVariable Long eventoId,
            // AGORA PODEMOS USAR ISTO!
            @AuthenticationPrincipal UserDetailsImpl userDetails 
    ) {
        // Verifica se o usuário está autenticado
        if (userDetails == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuário não autenticado."));
        }
        
        try {
            // Pega o ID do usuário logado diretamente do UserDetailsImpl
            Long usuarioId = userDetails.getId(); 
            
            Ingresso ingressoComprado = ingressoService.comprarIngresso(usuarioId, eventoId);
            return new ResponseEntity<>(new IngressoResponseDTO(ingressoComprado), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/meus-ingressos")
    public ResponseEntity<?> meusIngressos(
            // USA O USUÁRIO LOGADO AQUI TAMBÉM
            @AuthenticationPrincipal UserDetailsImpl userDetails 
    ) {
        if (userDetails == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuário não autenticado."));
        }
        
         try {
            Long usuarioId = userDetails.getId();
            List<Ingresso> ingressos = ingressoService.buscarIngressosPorUsuario(usuarioId);
            List<IngressoResponseDTO> dtos = ingressos.stream()
                    .map(IngressoResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (RuntimeException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}