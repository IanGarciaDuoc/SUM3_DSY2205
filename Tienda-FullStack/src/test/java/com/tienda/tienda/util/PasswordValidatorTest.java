package com.tienda.tienda.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    @Test
    void whenPasswordIsValid_thenNoErrors() {
        // Arrange
        String validPassword = "Password1@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(validPassword);

        // Assert
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void whenPasswordTooShort_thenInvalid() {
        // Arrange
        String shortPassword = "Pass1@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(shortPassword);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña debe tener al menos 8 caracteres"));
    }

    @Test
    void whenPasswordTooLong_thenInvalid() {
        // Arrange
        String longPassword = "Password1@Password1@Password1@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(longPassword);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña no debe exceder los 20 caracteres"));
    }

    @Test
    void whenPasswordHasNoLowerCase_thenInvalid() {
        // Arrange
        String noLowerCase = "PASSWORD1@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(noLowerCase);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos una letra minúscula"));
    }

    @Test
    void whenPasswordHasNoUpperCase_thenInvalid() {
        // Arrange
        String noUpperCase = "password1@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(noUpperCase);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos una letra mayúscula"));
    }

    @Test
    void whenPasswordHasNoNumber_thenInvalid() {
        // Arrange
        String noNumber = "Password@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(noNumber);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos un número"));
    }

    @Test
    void whenPasswordHasNoSpecialChar_thenInvalid() {
        // Arrange
        String noSpecialChar = "Password1";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(noSpecialChar);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos un carácter especial (@#$%^&+=)"));
    }

    @Test
    void whenPasswordHasWhitespace_thenInvalid() {
        // Arrange
        String withSpace = "Password 1@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(withSpace);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña no puede contener espacios en blanco"));
    }

    @Test
    void whenPasswordHasMultipleErrors_thenReturnsAllErrors() {
        // Arrange
        String multipleErrors = "pass"; // Corta, sin mayúscula, sin número y sin carácter especial

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(multipleErrors);

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().size() > 1);
        assertTrue(result.getErrors().contains("La contraseña debe tener al menos 8 caracteres"));
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos una letra mayúscula"));
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos un número"));
        assertTrue(result.getErrors().contains("La contraseña debe contener al menos un carácter especial (@#$%^&+=)"));
    }

    

    @Test
    void whenPasswordIsEmpty_thenInvalid() {
        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword("");

        // Assert
        assertFalse(result.isValid());
        assertTrue(result.getErrors().contains("La contraseña debe tener al menos 8 caracteres"));
    }
    
    @Test
    void whenPasswordHasAllRequiredElements_thenValid() {
        // Arrange - contraseña con todos los elementos requeridos
        String validPassword = "TestPass123@";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(validPassword);

        // Assert
        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void whenPasswordHasValidLength_thenNoLengthErrors() {
        // Arrange - contraseña de 12 caracteres (entre 8 y 20)
        String validLengthPassword = "TestPass123@#";

        // Act
        PasswordValidator.PasswordValidationResult result = 
            PasswordValidator.validatePassword(validLengthPassword);

        // Assert
        assertTrue(result.getErrors().stream()
            .noneMatch(error -> error.contains("al menos 8 caracteres") || 
                               error.contains("exceder los 20 caracteres")));
    }
}