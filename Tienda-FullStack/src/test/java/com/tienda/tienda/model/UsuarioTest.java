package com.tienda.tienda.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombreUsuario("testuser");
        usuario.setCorreo("testuser@example.com");
        usuario.setContrasena("securePassword123");
        usuario.setRol("ROLE_USER");
        usuario.setNombre("Test");
        usuario.setApellido("User");
        usuario.setTelefono("123456789");
    }

    @Test
    void testOnCreate() {
        // Ejecutar
        usuario.onCreate();

        // Verificar
        assertNotNull(usuario.getFechaCreacion(), "La fecha de creación no debe ser nula");
        assertNotNull(usuario.getFechaActualizacion(), "La fecha de actualización no debe ser nula");
        assertEquals(usuario.getFechaCreacion(), usuario.getFechaActualizacion(), "Las fechas de creación y actualización deben ser iguales al crearse");
    }

   

    @Test
    void testValidarCamposObligatorios() {
        // Configurar
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario("nuevoUsuario");
        nuevoUsuario.setCorreo("nuevoUsuario@example.com");
        nuevoUsuario.setContrasena("password123");
        nuevoUsuario.setRol("ROLE_ADMIN");

        // Verificar
        assertNotNull(nuevoUsuario.getNombreUsuario(), "El nombre de usuario no debe ser nulo");
        assertNotNull(nuevoUsuario.getCorreo(), "El correo no debe ser nulo");
        assertNotNull(nuevoUsuario.getContrasena(), "La contraseña no debe ser nula");
        assertNotNull(nuevoUsuario.getRol(), "El rol no debe ser nulo");
    }

    @Test
    void testAsignarRolCorrecto() {
        // Configurar
        usuario.setRol("ROLE_ADMIN");

        // Verificar
        assertEquals("ROLE_ADMIN", usuario.getRol(), "El rol del usuario debe ser ROLE_ADMIN");
    }

    @Test
    void testSetNombreUsuarioUnico() {
        // Configurar
        Usuario otroUsuario = new Usuario();
        otroUsuario.setNombreUsuario("testuser");

        // Verificar
        assertEquals("testuser", otroUsuario.getNombreUsuario(), "El nombre de usuario debe ser único y coincidir con el asignado");
    }

    @Test
    void testCamposOpcionales() {
        // Configurar
        usuario.setNombre("John");
        usuario.setApellido("Doe");
        usuario.setTelefono("987654321");

        // Verificar
        assertEquals("John", usuario.getNombre(), "El nombre debe coincidir con el asignado");
        assertEquals("Doe", usuario.getApellido(), "El apellido debe coincidir con el asignado");
        assertEquals("987654321", usuario.getTelefono(), "El teléfono debe coincidir con el asignado");
    }
}
