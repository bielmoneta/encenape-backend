package com.encenape.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.encenape.model.Usuario;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByResetToken(String resetToken);

    Optional<Usuario> findByEmailAndIdNot(String email, Long id);
}
