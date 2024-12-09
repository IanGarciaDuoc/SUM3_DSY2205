package com.tienda.tienda.controller;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsuarioById_Found() {
        // Configuración
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Ejecución
        ResponseEntity<Usuario> response = usuarioController.getUsuarioById(1L);

        // Verificación
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testuser", response.getBody().getNombreUsuario());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUsuarioById_NotFound() {
        // Configuración
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecución
        ResponseEntity<Usuario> response = usuarioController.getUsuarioById(1L);

        // Verificación
        assertEquals(404, response.getStatusCodeValue());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllUsuarios() {
        // Configuración
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario());
        usuarios.add(new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Ejecución
        ResponseEntity<List<Usuario>> response = usuarioController.getAllUsuarios();

        // Verificación
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(usuarioRepository, times(1)).findAll();
    }

   
    @Test
    void testCrearUsuario_UsuarioExistente() {
        // Configuración
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testuser");
        when(usuarioRepository.existsByNombreUsuario("testuser")).thenReturn(true);

        // Ejecución
        ResponseEntity<?> response = usuarioController.crearUsuario(usuario);

        // Verificación
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Nombre de usuario ya existe", response.getBody());
        verify(usuarioRepository, times(1)).existsByNombreUsuario("testuser");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testActualizarUsuario_Found() {
        // Configuración
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario usuarioDetails = new Usuario();
        usuarioDetails.setNombreUsuario("updatedUser");
        usuarioDetails.setCorreo("updated@example.com");

        // Ejecución
        ResponseEntity<?> response = usuarioController.actualizarUsuario(1L, usuarioDetails);

        // Verificación
        assertEquals(200, response.getStatusCodeValue());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void testActualizarUsuario_NotFound() {
        // Configuración
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecución
        ResponseEntity<?> response = usuarioController.actualizarUsuario(1L, new Usuario());

        // Verificación
        assertEquals(404, response.getStatusCodeValue());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testEliminarUsuario_Found() {
        // Configuración
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        // Ejecución
        ResponseEntity<?> response = usuarioController.eliminarUsuario(1L);

        // Verificación
        assertEquals(200, response.getStatusCodeValue());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void testEliminarUsuario_NotFound() {
        // Configuración
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Ejecución
        ResponseEntity<?> response = usuarioController.eliminarUsuario(1L);

        // Verificación
        assertEquals(404, response.getStatusCodeValue());
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
void testCrearUsuario_CorreoExistente() {
    // Configuración
    Usuario usuario = new Usuario();
    usuario.setCorreo("test@example.com");
    when(usuarioRepository.existsByCorreo("test@example.com")).thenReturn(true);

    // Ejecución
    ResponseEntity<?> response = usuarioController.crearUsuario(usuario);

    // Verificación
    assertEquals(400, response.getStatusCodeValue());
    assertEquals("Correo ya existe", response.getBody());
    verify(usuarioRepository, times(1)).existsByCorreo("test@example.com");
    verify(usuarioRepository, never()).save(any());
}
@Test
void testActualizarUsuario_ContrasenaCambiada() {
    // Configuración
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    usuario.setNombreUsuario("testuser");
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

    Usuario usuarioDetails = new Usuario();
    usuarioDetails.setContrasena("newPassword");

    when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
    when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

    // Ejecución
    ResponseEntity<?> response = usuarioController.actualizarUsuario(1L, usuarioDetails);

    // Verificación
    assertEquals(200, response.getStatusCodeValue());
    verify(passwordEncoder, times(1)).encode("newPassword");
    verify(usuarioRepository, times(1)).save(any(Usuario.class));
}
@Test
void testEliminarUsuario_Error() {
    // Configuración
    Usuario usuario = new Usuario();
    usuario.setId(1L);
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
    doThrow(new RuntimeException("Error eliminando usuario")).when(usuarioRepository).delete(usuario);

    // Ejecución
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        usuarioController.eliminarUsuario(1L);
    });

    // Verificación
    assertEquals("Error eliminando usuario", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(1L);
    verify(usuarioRepository, times(1)).delete(usuario);
}
@Test
void testGetUsuarioById_Exception() {
    // Configuración
    when(usuarioRepository.findById(1L)).thenThrow(new RuntimeException("Error buscando usuario"));

    // Ejecución
    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        usuarioController.getUsuarioById(1L);
    });

    // Verificación
    assertEquals("Error buscando usuario", exception.getMessage());
    verify(usuarioRepository, times(1)).findById(1L);
}
}
