package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;


import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired  
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            // Verificar si ya existe el usuario o correo
            if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
                return ResponseEntity.badRequest().body("Nombre de usuario ya existe");
            }
            if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
                return ResponseEntity.badRequest().body("Correo ya existe");
            }

            // Asegurarse que el rol tenga el prefijo ROLE_
            if (!usuario.getRol().startsWith("ROLE_")) {
                usuario.setRol("ROLE_" + usuario.getRol());
            }

            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            Usuario nuevoUsuario = usuarioRepository.save(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuarioDetails) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
                usuario.setCorreo(usuarioDetails.getCorreo());
                usuario.setNombre(usuarioDetails.getNombre());
                usuario.setApellido(usuarioDetails.getApellido());
                usuario.setRol(usuarioDetails.getRol());
                if (usuarioDetails.getContrasena() != null && !usuarioDetails.getContrasena().isEmpty()) {
                    usuario.setContrasena(passwordEncoder.encode(usuarioDetails.getContrasena()));
                }
                return ResponseEntity.ok(usuarioRepository.save(usuario));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuarioRepository.delete(usuario);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}
