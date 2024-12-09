package com.tienda.tienda.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private AuthenticationException mockAuthException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
    }

    @Test
    void commence_shouldReturnUnauthorizedResponse() throws Exception {
        // Ejecutar el método a probar
        jwtAuthenticationEntryPoint.commence(mockRequest, mockResponse, mockAuthException);
    
        // Verificar que se envió un error con el estado 401 y el mensaje esperado
        verify(mockResponse, times(1)).sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
    }
}
