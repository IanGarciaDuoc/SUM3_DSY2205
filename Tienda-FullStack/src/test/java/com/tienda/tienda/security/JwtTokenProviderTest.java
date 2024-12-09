package com.tienda.tienda.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private Authentication authentication;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        // Configurar propiedades necesarias para JWT
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 86400000); // 1 día

        userPrincipal = new UserPrincipal(
            1L,
            "testuser",
            "test@example.com",
            "password",
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            userPrincipal.getAuthorities()
        );
    }

    @Test
    void whenGenerateToken_thenSuccessful() {
        // Act
        String token = jwtTokenProvider.generateToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void whenGetUserIdFromValidToken_thenReturnsCorrectId() {
        // Arrange
        String token = jwtTokenProvider.generateToken(authentication);

        // Act
        Long userId = jwtTokenProvider.getUserIdFromJWT(token);

        // Assert
        assertEquals(userPrincipal.getId(), userId);
    }

    @Test
    void whenValidateToken_withValidToken_thenReturnsTrue() {
        // Arrange
        String token = jwtTokenProvider.generateToken(authentication);

        // Act & Assert
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void whenValidateToken_withExpiredToken_thenReturnsFalse() {
        // Arrange
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", 0); // Token expira inmediatamente
        String token = jwtTokenProvider.generateToken(authentication);

        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(token));
    }

   
    @Test
    void whenValidateToken_withMalformedToken_thenReturnsFalse() {
        // Arrange
        String malformedToken = "malformed.jwt.token";

        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(malformedToken));
    }

    @Test
    void whenValidateToken_withEmptyToken_thenReturnsFalse() {
        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(""));
    }

    @Test
    void whenValidateToken_withNullToken_thenReturnsFalse() {
        // Act & Assert
        assertFalse(jwtTokenProvider.validateToken(null));
    }

    @Test
    void whenGetUserIdFromJWT_withInvalidToken_thenThrowsException() {
        // Arrange
        String invalidToken = "invalid.jwt.token";

        // Act & Assert
        assertThrows(MalformedJwtException.class, () -> {
            jwtTokenProvider.getUserIdFromJWT(invalidToken);
        });
    }
}