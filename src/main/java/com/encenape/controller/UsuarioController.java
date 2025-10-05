package com.encenape.controller;

import com.encenape.dto.LoginRequestDTO;
import com.encenape.dto.UsuarioDTO;
import com.encenape.model.Usuario;
import com.encenape.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:3000") // frontend React
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuarioDTO);
            // Evita retornar a senha na resposta
            novoUsuario.setSenha(null);
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
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
}