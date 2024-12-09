package com.tienda.tienda.security;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String nombreUsuarioOCorreo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByNombreUsuarioOrCorreo(nombreUsuarioOCorreo, nombreUsuarioOCorreo)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username o email: " + nombreUsuarioOCorreo));

        return UserPrincipal.create(usuario);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + id));

        return UserPrincipal.create(usuario);
    }
}