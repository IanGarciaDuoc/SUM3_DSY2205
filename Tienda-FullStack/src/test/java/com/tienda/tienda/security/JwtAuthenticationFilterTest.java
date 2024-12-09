package com.tienda.tienda.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockFilterChain;

    @Mock
    private UserDetails mockUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternal_validToken_authenticationSet() throws ServletException, IOException {
        // Configurar mocks
        String jwt = "valid.jwt.token";
        Long userId = 123L;

        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(tokenProvider.validateToken(jwt)).thenReturn(true);
        when(tokenProvider.getUserIdFromJWT(jwt)).thenReturn(userId);
        when(customUserDetailsService.loadUserById(userId)).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn("testuser");
        when(mockUserDetails.getAuthorities()).thenReturn(null);

        // Ejecutar el filtro
        jwtAuthenticationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);

        // Verificar que se cargaron los detalles del usuario y se configuró la autenticación
        verify(customUserDetailsService, times(1)).loadUserById(userId);
        verify(mockFilterChain, times(1)).doFilter(mockRequest, mockResponse);

        UsernamePasswordAuthenticationToken authentication =
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        assert authentication.getPrincipal().equals(mockUserDetails);
    }

}
