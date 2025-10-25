package com.encenape.controller;

import com.encenape.dto.EsqueceuSenhaDTO; 
import com.encenape.dto.UsuarioUpdatePerfilDTO;
import java.util.Map;
import com.encenape.dto.ResetarSenhaDTO;
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
@CrossOrigin(origins = "http://localhost:4200") // frontend React
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
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarPerfil(@PathVariable Long id, @RequestBody UsuarioUpdatePerfilDTO updateDTO) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizarPerfil(id, updateDTO);
            return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));
        } catch (IllegalArgumentException e) {
            // Retorna 409 (Conflict) se o email já existir
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            // Retorna 404 (Not Found) se o usuário não existir
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
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

    @PostMapping("/esqueceu-senha")
    public ResponseEntity<String> forgotPassword(@RequestBody EsqueceuSenhaDTO esqueceuSenhaDTO) {
        usuarioService.solicitarRedefinicaoSenha(esqueceuSenhaDTO.getEmail());
        
        // Retorna uma mensagem genérica para segurança
        return ResponseEntity.ok("Se o email estiver cadastrado, um link de redefinição foi enviado.");
    }

    @PostMapping("/resetar-senha")
   public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetarSenhaDTO resetarSenhaDTO) {
    boolean sucesso = usuarioService.redefinirSenha(resetarSenhaDTO.getToken(), resetarSenhaDTO.getNovaSenha());

    if (sucesso) {
        // Agora estamos retornando um JSON
        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));
    } else {
        // E também retornamos um JSON para o erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Token inválido ou expirado."));
        }
    }

    //Para buscar os dados de um usuário (para a pág. de perfil)
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(new UsuarioResponseDTO(usuario))) // Converte para DTO seguro
                .orElse(ResponseEntity.notFound().build());
    }
}