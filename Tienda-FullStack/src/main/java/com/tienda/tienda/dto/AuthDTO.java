package com.tienda.tienda.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

public class AuthDTO {
    
    @Data
    public static class LoginRequest {
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Debe ser un correo válido")
        private String correo;

        @NotBlank(message = "La contraseña es obligatoria")
        private String contrasena;
    }

    @Data
    public static class SignUpRequest {
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        private String nombreUsuario;

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Debe ser un correo válido")
        private String correo;

        @NotBlank(message = "La contraseña es obligatoria")
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
        private String contrasena;

        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotBlank(message = "El apellido es obligatorio")
        private String apellido;

        private String telefono;
    }

    @Data
    public static class JwtResponse {
        private String token;
        private String tipo = "Bearer";
        private Long id;
        private String nombreUsuario;
        private String correo;
        private String rol;

        public JwtResponse(String token, Long id, String nombreUsuario, String correo, String rol) {
            this.token = token;
            this.id = id;
            this.nombreUsuario = nombreUsuario;
            this.correo = correo;
            this.rol = rol;
        }
    }

    @Data
    public static class MessageResponse {
        private String mensaje;

        public MessageResponse(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}