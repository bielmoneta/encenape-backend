package com.encenape.security;

import com.encenape.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections; 

public class UserDetailsImpl implements UserDetails {

    private Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public Long getId() {
        return usuario.getId();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, todos são "USER". No futuro, poderíamos ter ROLES (ADMIN, etc.)
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha(); // Retorna a senha HASHED do banco
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    // Métodos padrão do UserDetails 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}