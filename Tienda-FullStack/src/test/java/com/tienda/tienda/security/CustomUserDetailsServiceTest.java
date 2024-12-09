package com.tienda.tienda.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setCorreo("test@example.com");
        usuario.setContrasena("password123");
        usuario.setNombre("Test");
        usuario.setApellido("User");
        usuario.setRol("USER");
    }

    @Test
    void loadUserByUsername_WithValidUsername_ReturnsUserDetails() {
        // Arrange
        when(usuarioRepository.findByNombreUsuarioOrCorreo("testuser", "testuser"))
            .thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails instanceof UserPrincipal);
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        assertEquals(usuario.getId(), userPrincipal.getId());
    }

    
    @Test
    void loadUserByUsername_WithInvalidCredentials_ThrowsException() {
        // Arrange
        when(usuarioRepository.findByNombreUsuarioOrCorreo("invalid@example.com", "invalid@example.com"))
            .thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("invalid@example.com");
        });

        String expectedMessage = "Usuario no encontrado con username o email: invalid@example.com";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void loadUserById_WithValidId_ReturnsUserDetails() {
        // Arrange
        when(usuarioRepository.findById(1L))
            .thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserById(1L);

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof UserPrincipal);
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        assertEquals(1L, userPrincipal.getId());
        assertEquals("testuser", userPrincipal.getUsername());
    }

    @Test
    void loadUserById_WithInvalidId_ThrowsException() {
        // Arrange
        when(usuarioRepository.findById(999L))
            .thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserById(999L);
        });

        String expectedMessage = "Usuario no encontrado con id: 999";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}