package com.tienda.tienda.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tienda.tienda.security.UserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class SeguridadServiceTest {

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SeguridadService seguridadService;

    private UserPrincipal userPrincipal;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        userPrincipal = new UserPrincipal(
            1L, 
            "testuser",
            "test@example.com",
            "password",
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void whenEsUsuarioActual_withMatchingId_thenReturnsTrue() {
        // Arrange
        authentication = new TestingAuthenticationToken(userPrincipal, null);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Act
        boolean result = seguridadService.esUsuarioActual(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void whenEsUsuarioActual_withDifferentId_thenReturnsFalse() {
        // Arrange
        authentication = new TestingAuthenticationToken(userPrincipal, null);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Act
        boolean result = seguridadService.esUsuarioActual(2L);

        // Assert
        assertFalse(result);
    }

    @Test
    void whenEsUsuarioActual_withNullAuthentication_thenReturnsFalse() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(null);

        // Act
        boolean result = seguridadService.esUsuarioActual(1L);

        // Assert
        assertFalse(result);
    }

   
}