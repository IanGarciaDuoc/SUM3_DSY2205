package com.tienda.tienda.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class UsuarioDTO {
    
    @Data
    public static class CambiarContrasenaRequest {
        @NotBlank(message = "La contraseña actual es obligatoria")
        private String contrasenaActual;

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "La contraseña debe cumplir con los siguientes criterios:\n" +
                    "- Longitud mínima de 8 caracteres\n" +
                    "- Longitud máxima de 20 caracteres\n" +
                    "- Al menos una letra minúscula\n" +
                    "- Al menos una letra mayúscula\n" +
                    "- Al menos un número\n" +
                    "- Al menos un carácter especial (@#$%^&+=)\n" +
                    "- No puede contener espacios en blanco"
        )
        private String nuevaContrasena;

        @NotBlank(message = "La confirmación de contraseña es obligatoria")
        private String confirmarContrasena;
    }
}