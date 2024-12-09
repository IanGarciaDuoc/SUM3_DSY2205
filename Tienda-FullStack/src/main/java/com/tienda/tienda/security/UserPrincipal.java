package com.tienda.tienda.security;

import com.tienda.tienda.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String nombreUsuario, String correo, String contrasena,
                        Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.authorities = authorities;
    }

    public static UserPrincipal create(Usuario usuario) {
        String roleWithPrefix = usuario.getRol().startsWith("ROLE_") ? 
            usuario.getRol() : "ROLE_" + usuario.getRol();
            
        List<GrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority(roleWithPrefix)
        );

        return new UserPrincipal(
            usuario.getId(),
            usuario.getNombreUsuario(),
            usuario.getCorreo(),
            usuario.getContrasena(),
            authorities
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

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