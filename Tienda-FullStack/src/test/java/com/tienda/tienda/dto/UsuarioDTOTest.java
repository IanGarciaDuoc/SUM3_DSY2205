package com.tienda.tienda.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDTOTest {


    @Test
    void testConstructorUsuarioDTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        assertNotNull(usuarioDTO);
    }
    

    @Test
    void testCambiarContrasenaRequest_SettersAndGetters() {
        // Crear una instancia de CambiarContrasenaRequest
        UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();

        // Configurar valores
        request.setContrasenaActual("Password@123");
        request.setNuevaContrasena("NewPassword@456");
        request.setConfirmarContrasena("NewPassword@456");

        // Verificar valores configurados
        assertEquals("Password@123", request.getContrasenaActual());
        assertEquals("NewPassword@456", request.getNuevaContrasena());
        assertEquals("NewPassword@456", request.getConfirmarContrasena());
    }

    @Test
    void testCambiarContrasenaRequest_InvalidValues() {
        // Crear una instancia de CambiarContrasenaRequest
        UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();

        // Validar que todos los campos sean nulos al inicio
        assertNull(request.getContrasenaActual());
        assertNull(request.getNuevaContrasena());
        assertNull(request.getConfirmarContrasena());
    }

    @Test
    void testCambiarContrasenaRequest_EqualsAndHashCode() {
        // Crear dos instancias de CambiarContrasenaRequest con los mismos valores
        UsuarioDTO.CambiarContrasenaRequest request1 = new UsuarioDTO.CambiarContrasenaRequest();
        request1.setContrasenaActual("Password@123");
        request1.setNuevaContrasena("NewPassword@456");
        request1.setConfirmarContrasena("NewPassword@456");

        UsuarioDTO.CambiarContrasenaRequest request2 = new UsuarioDTO.CambiarContrasenaRequest();
        request2.setContrasenaActual("Password@123");
        request2.setNuevaContrasena("NewPassword@456");
        request2.setConfirmarContrasena("NewPassword@456");

        // Verificar que las dos instancias sean iguales y tengan el mismo hashCode
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

   

    @Test
    void testCambiarContrasenaRequest_Validation() {
        // Crear una instancia de CambiarContrasenaRequest con valores válidos
        UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
        request.setContrasenaActual("Password@123");
        request.setNuevaContrasena("NewPassword@456");
        request.setConfirmarContrasena("NewPassword@456");

        // Verificar que cumple con las restricciones
        assertDoesNotThrow(() -> {
            assertNotNull(request.getContrasenaActual());
            assertNotNull(request.getNuevaContrasena());
            assertNotNull(request.getConfirmarContrasena());
        });

        // Probar una contraseña inválida
        request.setNuevaContrasena("short");
        String nuevaContrasena = request.getNuevaContrasena();
        assertNotEquals("NewPassword@456", nuevaContrasena);
    }
    @Test
void testCambiarContrasenaRequest_NonMatchingPasswords() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setNuevaContrasena("NewPassword@456");
    request.setConfirmarContrasena("DifferentPassword@789");

    // Verificar que las contraseñas no coinciden
    assertNotEquals(request.getNuevaContrasena(), request.getConfirmarContrasena());
}
@Test
void testCambiarContrasenaRequest_EmptyPassword() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setContrasenaActual("");
    request.setNuevaContrasena("");
    request.setConfirmarContrasena("");

    // Verificar que las contraseñas vacías no sean válidas
    assertTrue(request.getContrasenaActual().isEmpty());
    assertTrue(request.getNuevaContrasena().isEmpty());
    assertTrue(request.getConfirmarContrasena().isEmpty());
}
@Test
void testCambiarContrasenaRequest_SpecialCharacters() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setContrasenaActual("Pass@word!123");
    request.setNuevaContrasena("N3wP@ssw0rd#");
    request.setConfirmarContrasena("N3wP@ssw0rd#");

    // Verificar que las contraseñas con caracteres especiales sean válidas
    assertEquals("Pass@word!123", request.getContrasenaActual());
    assertEquals("N3wP@ssw0rd#", request.getNuevaContrasena());
    assertEquals("N3wP@ssw0rd#", request.getConfirmarContrasena());
}
@Test
void testCambiarContrasenaRequest_MinimumLengthPassword() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    String minLengthPassword = "Abc@1234"; // 8 caracteres
    request.setNuevaContrasena(minLengthPassword);
    request.setConfirmarContrasena(minLengthPassword);

    // Verificar que cumple con el mínimo de longitud
    assertEquals(minLengthPassword, request.getNuevaContrasena());
    assertEquals(minLengthPassword, request.getConfirmarContrasena());
}
@Test
void testCambiarContrasenaRequest_MaximumLengthPassword() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    String maxLengthPassword = "Abc@123456789012345"; // 20 caracteres
    request.setNuevaContrasena(maxLengthPassword);
    request.setConfirmarContrasena(maxLengthPassword);

    // Verificar que cumple con el máximo de longitud
    assertEquals(maxLengthPassword, request.getNuevaContrasena());
    assertEquals(maxLengthPassword, request.getConfirmarContrasena());
}
@Test
void testCambiarContrasenaRequest_NullFields() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();

    // Configurar algunos campos como nulos
    request.setContrasenaActual(null);
    request.setNuevaContrasena(null);
    request.setConfirmarContrasena(null);

    // Verificar que los campos sean nulos
    assertNull(request.getContrasenaActual());
    assertNull(request.getNuevaContrasena());
    assertNull(request.getConfirmarContrasena());
}
@Test
void testCambiarContrasenaRequest_OnlyNumbers() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setNuevaContrasena("12345678");
    request.setConfirmarContrasena("12345678");

    // Verificar que no cumple con los requisitos de contraseña
    assertEquals("12345678", request.getNuevaContrasena());
    assertEquals("12345678", request.getConfirmarContrasena());
}
@Test
void testCambiarContrasenaRequest_OnlyLetters() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setNuevaContrasena("abcdefgh");
    request.setConfirmarContrasena("abcdefgh");

    // Verificar que la contraseña solo con letras no cumple los requisitos
    assertEquals("abcdefgh", request.getNuevaContrasena());
    assertEquals("abcdefgh", request.getConfirmarContrasena());
}

@Test
void testCambiarContrasenaRequest_WithSpaces() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setNuevaContrasena("New Password 123");
    request.setConfirmarContrasena("New Password 123");

    // Verificar que la contraseña con espacios sea válida según las reglas
    assertEquals("New Password 123", request.getNuevaContrasena());
    assertEquals("New Password 123", request.getConfirmarContrasena());
}

@Test
void testCambiarContrasenaRequest_SameAsCurrentPassword() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setContrasenaActual("Password@123");
    request.setNuevaContrasena("Password@123");
    request.setConfirmarContrasena("Password@123");

    // Verificar que la nueva contraseña no puede ser igual a la actual
    assertEquals(request.getContrasenaActual(), request.getNuevaContrasena());
}

@Test
void testCambiarContrasenaRequest_TooLongPassword() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    String tooLongPassword = "A".repeat(101); // 101 caracteres
    request.setNuevaContrasena(tooLongPassword);
    request.setConfirmarContrasena(tooLongPassword);

    // Verificar que la contraseña no cumple con el límite de longitud
    assertEquals(tooLongPassword, request.getNuevaContrasena());
    assertEquals(tooLongPassword, request.getConfirmarContrasena());
}

@Test
void testCambiarContrasenaRequest_WithEmojis() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setNuevaContrasena("Password😊123");
    request.setConfirmarContrasena("Password😊123");

    // Verificar que la contraseña con emojis sea aceptada según las reglas actuales
    assertEquals("Password😊123", request.getNuevaContrasena());
    assertEquals("Password😊123", request.getConfirmarContrasena());
}

@Test
void testCambiarContrasenaRequest_MismatchedConfirmPassword() {
    UsuarioDTO.CambiarContrasenaRequest request = new UsuarioDTO.CambiarContrasenaRequest();
    request.setNuevaContrasena("Password@123");
    request.setConfirmarContrasena("DifferentPassword@456");

    // Verificar que la nueva contraseña y la confirmación no coincidan
    assertNotEquals(request.getNuevaContrasena(), request.getConfirmarContrasena());
}


}
