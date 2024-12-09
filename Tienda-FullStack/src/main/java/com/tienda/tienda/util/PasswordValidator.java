package com.tienda.tienda.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PasswordValidator {
    
    public static class PasswordValidationResult {
        private boolean isValid;
        private List<String> errors;

        public PasswordValidationResult() {
            this.isValid = true;
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            this.isValid = false;
            this.errors.add(error);
        }

        public boolean isValid() {
            return isValid;
        }

        public List<String> getErrors() {
            return errors;
        }
    }

    public static PasswordValidationResult validatePassword(String password) {
        PasswordValidationResult result = new PasswordValidationResult();

        // 1. Validar longitud mínima
        if (password.length() < 8) {
            result.addError("La contraseña debe tener al menos 8 caracteres");
        }

        // 2. Validar longitud máxima
        if (password.length() > 20) {
            result.addError("La contraseña no debe exceder los 20 caracteres");
        }

        // 3. Validar letra minúscula
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            result.addError("La contraseña debe contener al menos una letra minúscula");
        }

        // 4. Validar letra mayúscula
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            result.addError("La contraseña debe contener al menos una letra mayúscula");
        }

        // 5. Validar número
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            result.addError("La contraseña debe contener al menos un número");
        }

        // 6. Validar carácter especial
        if (!Pattern.compile("[@#$%^&+=]").matcher(password).find()) {
            result.addError("La contraseña debe contener al menos un carácter especial (@#$%^&+=)");
        }

        // 7. Validar espacios en blanco
        if (password.contains(" ")) {
            result.addError("La contraseña no puede contener espacios en blanco");
        }

        return result;
    }
}