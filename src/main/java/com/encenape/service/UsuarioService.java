package com.encenape.service;

import com.encenape.dto.UsuarioRequestDTO;
import com.encenape.model.Usuario;
import com.encenape.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Optional<Usuario> atualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {
        // 1. Busca o usuário no banco de dados pelo ID
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        // 2. Se o usuário existir, atualiza seus dados
        if (usuarioOptional.isPresent()) {
            Usuario usuarioExistente = usuarioOptional.get();

            // Atualiza apenas os campos que vieram no DTO
            // (Poderíamos adicionar validações aqui, como não permitir alterar o email)
            usuarioExistente.setNome(requestDTO.getNome());
            
            // Nota: Por simplicidade, não estamos permitindo a alteração de email ou senha aqui.
            // A alteração de senha deveria ter seu próprio endpoint e fluxo de segurança.

            // 3. Salva o usuário com os dados atualizados e o retorna
            return Optional.of(usuarioRepository.save(usuarioExistente));
        }

        // 4. Retorna vazio se o usuário não foi encontrado
        return Optional.empty();
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
}