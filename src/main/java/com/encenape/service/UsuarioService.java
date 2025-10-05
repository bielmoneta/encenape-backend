package com.encenape.service;

import com.encenape.dto.UsuarioDTO;
import com.encenape.model.Usuario;
import com.encenape.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrarUsuario(UsuarioDTO usuarioDTO) {
        // Verifica se o email já existe no banco
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioDTO.getNome());
        novoUsuario.setEmail(usuarioDTO.getEmail());
        // Criptografa a senha antes de salvar
        novoUsuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        return usuarioRepository.save(novoUsuario);
    }

    public Optional<Usuario> validarLogin(String email, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // Compara a senha enviada com a senha criptografada no banco
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return usuarioOptional;
            }
        }

        return Optional.empty(); // Retorna vazio se o email não for encontrado ou a senha estiver incorreta
    }
}