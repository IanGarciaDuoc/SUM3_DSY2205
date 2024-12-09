package com.tienda.tienda.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testLoginRequest_ValidData() {
        AuthDTO.LoginRequest loginRequest = new AuthDTO.LoginRequest();
        loginRequest.setCorreo("test@example.com");
        loginRequest.setContrasena("password123");

        Set<ConstraintViolation<AuthDTO.LoginRequest>> violations = validator.validate(loginRequest);

        // Debe pasar sin violaciones
        assertTrue(violations.isEmpty());
    }

    @Test
    void testLoginRequest_InvalidEmail() {
        AuthDTO.LoginRequest loginRequest = new AuthDTO.LoginRequest();
        loginRequest.setCorreo("invalid-email");
        loginRequest.setContrasena("password123");

        Set<ConstraintViolation<AuthDTO.LoginRequest>> violations = validator.validate(loginRequest);

        // Debe haber una violación para el correo inválido
        assertEquals(1, violations.size());
        assertEquals("Debe ser un correo válido", violations.iterator().next().getMessage());
    }

    @Test
    void testSignUpRequest_ValidData() {
        AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
        signUpRequest.setNombreUsuario("validUser");
        signUpRequest.setCorreo("user@example.com");
        signUpRequest.setContrasena("Password@123");
        signUpRequest.setNombre("John");
        signUpRequest.setApellido("Doe");
        signUpRequest.setTelefono("123456789");

        Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

        // Debe pasar sin violaciones
        assertTrue(violations.isEmpty());
    }

    @Test
    void testSignUpRequest_InvalidPassword() {
        AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
        signUpRequest.setNombreUsuario("validUser");
        signUpRequest.setCorreo("user@example.com");
        signUpRequest.setContrasena("weakpass"); // Contraseña inválida
        signUpRequest.setNombre("John");
        signUpRequest.setApellido("Doe");
        signUpRequest.setTelefono("123456789");

        Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

        // Debe haber una violación para la contraseña inválida
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("La contraseña debe cumplir con los siguientes criterios"));
    }

    @Test
    void testSignUpRequest_MissingFields() {
        AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
        // No se establece ningún valor para los campos

        Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

        // Debe haber una violación por cada campo obligatorio
        assertEquals(5, violations.size());
    }

    @Test
    void testJwtResponse() {
        AuthDTO.JwtResponse jwtResponse = new AuthDTO.JwtResponse("token123", 1L, "user", "user@example.com", "USER");

        // Verificar que los valores sean correctos
        assertEquals("token123", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("user", jwtResponse.getNombreUsuario());
        assertEquals("user@example.com", jwtResponse.getCorreo());
        assertEquals("USER", jwtResponse.getRol());
    }

    @Test
    void testMessageResponse() {
        AuthDTO.MessageResponse messageResponse = new AuthDTO.MessageResponse("Test message");

        // Verificar que el mensaje sea correcto
        assertEquals("Test message", messageResponse.getMensaje());
    }
    @Test
void testLoginRequest_BlankFields() {
    AuthDTO.LoginRequest loginRequest = new AuthDTO.LoginRequest();
    loginRequest.setCorreo(""); // Campo en blanco
    loginRequest.setContrasena(""); // Campo en blanco

    Set<ConstraintViolation<AuthDTO.LoginRequest>> violations = validator.validate(loginRequest);

    // Debe haber dos violaciones: correo y contraseña obligatorios
    assertEquals(2, violations.size());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El correo es obligatorio")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La contraseña es obligatoria")));
}

@Test
void testSignUpRequest_ShortUsername() {
    AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
    signUpRequest.setNombreUsuario("a"); // Demasiado corto
    signUpRequest.setCorreo("user@example.com");
    signUpRequest.setContrasena("Password@123");
    signUpRequest.setNombre("John");
    signUpRequest.setApellido("Doe");
    signUpRequest.setTelefono("123456789");

    Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

    // Debe haber una violación para el nombre de usuario
    assertEquals(1, violations.size());
    assertEquals("El nombre de usuario debe tener entre 3 y 50 caracteres", 
                 violations.iterator().next().getMessage());
}

@Test
void testSignUpRequest_LongUsername() {
    AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
    signUpRequest.setNombreUsuario("a".repeat(51)); // Demasiado largo
    signUpRequest.setCorreo("user@example.com");
    signUpRequest.setContrasena("Password@123");
    signUpRequest.setNombre("John");
    signUpRequest.setApellido("Doe");
    signUpRequest.setTelefono("123456789");

    Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

    // Debe haber una violación para el nombre de usuario
    assertEquals(1, violations.size());
    assertEquals("El nombre de usuario debe tener entre 3 y 50 caracteres", 
                 violations.iterator().next().getMessage());
}

@Test
void testSignUpRequest_InvalidEmail() {
    AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
    signUpRequest.setNombreUsuario("validUser");
    signUpRequest.setCorreo("invalid-email"); // Correo inválido
    signUpRequest.setContrasena("Password@123");
    signUpRequest.setNombre("John");
    signUpRequest.setApellido("Doe");
    signUpRequest.setTelefono("123456789");

    Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

    // Debe haber una violación para el correo inválido
    assertEquals(1, violations.size());
    assertEquals("Debe ser un correo válido", violations.iterator().next().getMessage());
}

@Test
void testSignUpRequest_NullFields() {
    AuthDTO.SignUpRequest signUpRequest = new AuthDTO.SignUpRequest();
    signUpRequest.setNombreUsuario(null); // Null en lugar de en blanco
    signUpRequest.setCorreo(null); // Null en lugar de en blanco
    signUpRequest.setContrasena(null); // Null en lugar de en blanco
    signUpRequest.setNombre(null); // Null en lugar de en blanco
    signUpRequest.setApellido(null); // Null en lugar de en blanco

    Set<ConstraintViolation<AuthDTO.SignUpRequest>> violations = validator.validate(signUpRequest);

    // Debe haber cinco violaciones por campos obligatorios
    assertEquals(5, violations.size());
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre de usuario es obligatorio")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El correo es obligatorio")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("La contraseña es obligatoria")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El nombre es obligatorio")));
    assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("El apellido es obligatorio")));
}



}
