package com.tienda.tienda.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tienda.tienda.dto.AuthDTO;
import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import com.tienda.tienda.security.JwtTokenProvider;
import com.tienda.tienda.security.UserPrincipal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthController authController;

    private AuthDTO.LoginRequest loginRequest;
    private AuthDTO.SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        // Setup para login
        loginRequest = new AuthDTO.LoginRequest();
        loginRequest.setCorreo("test@example.com");
        loginRequest.setContrasena("Password123@");

        // Setup para registro
        signUpRequest = new AuthDTO.SignUpRequest();
        signUpRequest.setNombreUsuario("testuser");
        signUpRequest.setCorreo("test@example.com");
        signUpRequest.setContrasena("Password123@");
        signUpRequest.setNombre("Test");
        signUpRequest.setApellido("User");
        signUpRequest.setTelefono("1234567890");
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnsJwtToken() {
        // Arrange
        UserPrincipal userPrincipal = new UserPrincipal(
            1L,
            "testuser",
            "test@example.com",
            "Password123@",
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any(Authentication.class)))
            .thenReturn("test-jwt-token");

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof AuthDTO.JwtResponse);
        AuthDTO.JwtResponse jwtResponse = (AuthDTO.JwtResponse) response.getBody();
        assertEquals("test-jwt-token", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("testuser", jwtResponse.getNombreUsuario());
    }

    @Test
    void whenLoginWithInvalidCredentials_thenReturnsBadRequest() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new RuntimeException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof AuthDTO.MessageResponse);
        assertEquals(
            "Error: Usuario o contraseña incorrectos",
            ((AuthDTO.MessageResponse) response.getBody()).getMensaje()
        );
    }

    @Test
    void whenRegisterWithValidData_thenReturnsSuccess() {
        // Arrange
        when(usuarioRepository.existsByNombreUsuario(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(usuarioRepository).save(any(Usuario.class));
        assertTrue(response.getBody() instanceof AuthDTO.MessageResponse);
        assertEquals(
            "Usuario registrado exitosamente",
            ((AuthDTO.MessageResponse) response.getBody()).getMensaje()
        );
    }

    @Test
    void whenRegisterWithExistingUsername_thenReturnsBadRequest() {
        // Arrange
        when(usuarioRepository.existsByNombreUsuario("testuser")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        assertTrue(response.getBody() instanceof AuthDTO.MessageResponse);
        assertEquals(
            "Error: El nombre de usuario ya está en uso",
            ((AuthDTO.MessageResponse) response.getBody()).getMensaje()
        );
    }

    @Test
    void whenRegisterWithExistingEmail_thenReturnsBadRequest() {
        // Arrange
        when(usuarioRepository.existsByNombreUsuario(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCorreo("test@example.com")).thenReturn(true);

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        assertTrue(response.getBody() instanceof AuthDTO.MessageResponse);
        assertEquals(
            "Error: El correo ya está en uso",
            ((AuthDTO.MessageResponse) response.getBody()).getMensaje()
        );
    }

    @Test
    void whenRegisterWithWeakPassword_thenReturnsBadRequest() {
        // Arrange
        signUpRequest.setContrasena("weak");

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        verify(usuarioRepository, never()).save(any(Usuario.class));
        assertTrue(response.getBody() instanceof AuthDTO.MessageResponse);
        assertTrue(
            ((AuthDTO.MessageResponse) response.getBody()).getMensaje()
                .contains("Error de validación en la contraseña")
        );
    }

    @Test
    void whenRegisterAdminUser_thenSetsAdminRole() {
        // Arrange
        signUpRequest.setNombreUsuario("admin");
        when(usuarioRepository.existsByNombreUsuario(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCorreo(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(usuarioRepository).save(argThat(usuario -> 
            "ADMIN".equals(usuario.getRol())
        ));
    }
}