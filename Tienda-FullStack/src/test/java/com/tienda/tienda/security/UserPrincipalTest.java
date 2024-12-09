package com.tienda.tienda.security;

import static org.junit.jupiter.api.Assertions.*;

import com.tienda.tienda.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;

class UserPrincipalTest {

    private Usuario usuario;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        // Crear usuario de prueba
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("testuser");
        usuario.setCorreo("test@example.com");
        usuario.setContrasena("password123");
        usuario.setRol("ADMIN");
        
        // Crear UserPrincipal directamente
        userPrincipal = new UserPrincipal(
            1L,
            "testuser",
            "test@example.com",
            "password123",
            Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }


    @Test
    void whenGetAuthorities_thenReturnsCorrectAuthorities() {
        // Act
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void whenGetPassword_thenReturnsPassword() {
        // Act & Assert
        assertEquals("password123", userPrincipal.getPassword());
    }

    @Test
    void whenGetUsername_thenReturnsUsername() {
        // Act & Assert
        assertEquals("testuser", userPrincipal.getUsername());
    }

    @Test
    void whenGetId_thenReturnsCorrectId() {
        // Act & Assert
        assertEquals(1L, userPrincipal.getId());
    }

    @Test
    void whenCheckAccountNonExpired_thenReturnsTrue() {
        // Act & Assert
        assertTrue(userPrincipal.isAccountNonExpired());
    }

    @Test
    void whenCheckAccountNonLocked_thenReturnsTrue() {
        // Act & Assert
        assertTrue(userPrincipal.isAccountNonLocked());
    }

    @Test
    void whenCheckCredentialsNonExpired_thenReturnsTrue() {
        // Act & Assert
        assertTrue(userPrincipal.isCredentialsNonExpired());
    }

    @Test
    void whenCheckEnabled_thenReturnsTrue() {
        // Act & Assert
        assertTrue(userPrincipal.isEnabled());
    }

    @Test
    void whenCreateFromUserWithoutRolePrefix_thenAddsPrefix() {
        // Arrange
        usuario.setRol("ADMIN"); // Sin el prefijo ROLE_

        // Act
        UserPrincipal result = UserPrincipal.create(usuario);

        // Assert
        assertTrue(result.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void whenCreateFromUserWithRolePrefix_thenKeepsPrefix() {
        // Arrange
        usuario.setRol("ROLE_ADMIN"); // Ya tiene el prefijo

        // Act
        UserPrincipal result = UserPrincipal.create(usuario);

        // Assert
        assertTrue(result.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void whenCreateWithNullValues_thenHandlesGracefully() {
        // Arrange
        Usuario usuarioNulo = new Usuario();
        usuarioNulo.setRol("USER");

        // Act
        UserPrincipal result = UserPrincipal.create(usuarioNulo);

        // Assert
        assertNull(result.getId());
        assertNull(result.getUsername());
        assertNull(result.getPassword());
        assertNotNull(result.getAuthorities());
        assertTrue(result.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}