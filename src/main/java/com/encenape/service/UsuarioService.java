package com.encenape.service;

import com.encenape.dto.UsuarioRequestDTO;
import com.encenape.dto.UsuarioUpdatePerfilDTO;
import com.encenape.model.Usuario;
import com.encenape.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Usuario cadastrarUsuario(UsuarioRequestDTO requestDTO) { // Recebe o DTO de requisição
        // Verifica se o email já existe no banco
        if (usuarioRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        // Mapeia os dados do DTO de requisição para a Entidade
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(requestDTO.getNome());
        novoUsuario.setEmail(requestDTO.getEmail());
        
        // Criptografa a senha antes de salvar
        novoUsuario.setSenha(passwordEncoder.encode(requestDTO.getSenha()));

        return usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> validarLogin(String email, String senha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())) {
                return usuarioOptional;
            }
        }
        return Optional.empty();
    }

   public Usuario atualizarPerfil(Long id, UsuarioUpdatePerfilDTO updateDTO) {
        // 1. Busca o usuário
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Verifica se o novo email já está em uso por OUTRO usuário
        Optional<Usuario> conflitoEmail = usuarioRepository.findByEmailAndIdNot(updateDTO.getEmail(), id);
        if (conflitoEmail.isPresent()) {
            throw new IllegalArgumentException("Este email já está em uso por outra conta.");
        }

        // 3. Atualiza os dados
        usuarioExistente.setNome(updateDTO.getNome());
        usuarioExistente.setEmail(updateDTO.getEmail());

        // 4. Salva no banco
        return usuarioRepository.save(usuarioExistente);
    }

    public boolean deletarUsuario(Long id) {
        // 1. Verifica se um usuário com o ID fornecido existe no banco de dados.
        // Usar existsById é mais eficiente do que findById se você só precisa saber se ele existe.
        if (usuarioRepository.existsById(id)) {
            // 2. Se existir, deleta o usuário.
            usuarioRepository.deleteById(id);
            // 3. Retorna 'true' para indicar sucesso na operação.
            return true;
        }
        
        // 4. Se o usuário não existir, retorna 'false'.
        return false;
    }

    public void solicitarRedefinicaoSenha(String email) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            
            // 1. Gera um token aleatório
            String token = UUID.randomUUID().toString();
            
            // 2. Define a expiração (ex: 10 minutos)
            usuario.setResetToken(token);
            usuario.setResetTokenExpiry(LocalDateTime.now().plusMinutes(10));
            
            // 3. Salva o token no banco
            usuarioRepository.save(usuario);
            
            // 4. Envia o email
            emailService.sendPasswordResetEmail(usuario.getEmail(), token);
        }
    }

    public boolean redefinirSenha(String token, String novaSenha) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByResetToken(token);

        if (usuarioOptional.isEmpty()) {
            return false; // Token não encontrado
        }

        Usuario usuario = usuarioOptional.get();

        // Verifica se o token expirou
        if (usuario.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            // Opcional: Limpe o token expirado
            usuario.setResetToken(null);
            usuario.setResetTokenExpiry(null);
            usuarioRepository.save(usuario);
            return false; // Token expirado
        }

        // Sucesso! Vamos redefinir a senha
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        
        // Invalida o token para que não possa ser usado novamente
        usuario.setResetToken(null);
        usuario.setResetTokenExpiry(null);
        
        usuarioRepository.save(usuario);
        return true;
    }
}