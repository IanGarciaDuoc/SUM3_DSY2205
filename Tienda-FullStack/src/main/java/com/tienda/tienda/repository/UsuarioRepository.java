package com.tienda.tienda.repository;

import com.tienda.tienda.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombreUsuarioOrCorreo(String nombreUsuario, String correo);
    Boolean existsByNombreUsuario(String nombreUsuario);
    Boolean existsByCorreo(String correo);
}