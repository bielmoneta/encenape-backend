package com.encenape.controller;

import com.encenape.dto.LoginRequestDTO;
import com.encenape.dto.UsuarioRequestDTO;
import com.encenape.dto.UsuarioResponseDTO;
import com.encenape.model.Usuario;
import com.encenape.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:3000") // frontend React
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioRequestDTO requestDTO) {
        try {
            // 1. Chama o Service para executar a lógica de negócio
            Usuario novoUsuario = usuarioService.cadastrarUsuario(requestDTO);
            
            // 2. Converte a Entidade (Model) para um DTO de Resposta seguro
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(novoUsuario);
            
            // 3. Retorna o DTO de Resposta
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/logar")
    public ResponseEntity<?> logar(@RequestBody LoginRequestDTO loginRequest) {
        Optional<Usuario> usuarioOptional = usuarioService.validarLogin(loginRequest.getEmail(), loginRequest.getSenha());

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            return ResponseEntity.ok(Map.of("message", "Login bem-sucedido!", "usuarioId", usuario.getId(), "nome", usuario.getNome()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
        }
    }

    // Endpoint para LISTAR todos os usuários
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        // 1. Chama o Service para buscar a lista de Entidades
        List<Usuario> usuarios = usuarioService.listarTodos();
        
        // 2. Converte a LISTA de Entidades para uma LISTA de DTOs de Resposta
        List<UsuarioResponseDTO> responseDTOs = usuarios.stream()
                .map(UsuarioResponseDTO::new) // Para cada usuário na lista, cria um new UsuarioResponseDTO(usuario)
                .collect(Collectors.toList());
                
        // 3. Retorna a lista de DTOs
        return ResponseEntity.ok(responseDTOs);
    }
    
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO requestDTO) {
        // 1. Chama o Service para tentar atualizar o usuário
        Optional<Usuario> usuarioAtualizadoOptional = usuarioService.atualizarUsuario(id, requestDTO);

        // 2. Verifica se o usuário foi encontrado e atualizado
        if (usuarioAtualizadoOptional.isPresent()) {
            // Se sim, converte o resultado para um DTO de resposta seguro
            UsuarioResponseDTO responseDTO = new UsuarioResponseDTO(usuarioAtualizadoOptional.get());
            return ResponseEntity.ok(responseDTO);
        } else {
            // Se não, retorna um erro 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário com ID " + id + " não encontrado.");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        // 1. Chama o Service para tentar deletar o usuário
        boolean deletado = usuarioService.deletarUsuario(id);

        // 2. Verifica se a operação de deleção foi bem-sucedida
        if (deletado) {
            // Se sim, retorna o status 204 No Content, que é o padrão para DELETE bem-sucedido.
            // Não há necessidade de enviar um corpo de resposta.
            return ResponseEntity.noContent().build();
        } else {
            // Se não, retorna um erro 404 (Not Found)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário com ID " + id + " não encontrado.");
        }
    }
}