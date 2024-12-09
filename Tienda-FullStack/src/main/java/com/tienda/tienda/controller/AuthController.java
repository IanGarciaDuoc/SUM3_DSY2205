
// AuthController.java
package com.tienda.tienda.controller;

import com.tienda.tienda.dto.AuthDTO.*;
import com.tienda.tienda.model.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import com.tienda.tienda.security.JwtTokenProvider;
import com.tienda.tienda.security.UserPrincipal;
import com.tienda.tienda.util.PasswordValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getCorreo(),
                    loginRequest.getContrasena()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getUsername(),
                userPrincipal.getAuthorities().iterator().next().getAuthority()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: Usuario o contraseña incorrectos"));
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        // Validar nombre de usuario único
        if (usuarioRepository.existsByNombreUsuario(signUpRequest.getNombreUsuario())) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: El nombre de usuario ya está en uso"));
        }

        // Validar correo único
        if (usuarioRepository.existsByCorreo(signUpRequest.getCorreo())) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: El correo ya está en uso"));
        }

        // Validar contraseña
        PasswordValidator.PasswordValidationResult passwordValidation = 
            PasswordValidator.validatePassword(signUpRequest.getContrasena());
        
        if (!passwordValidation.isValid()) {
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error de validación en la contraseña:\n" + 
                    String.join("\n", passwordValidation.getErrors())));
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(signUpRequest.getNombreUsuario());
        usuario.setCorreo(signUpRequest.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(signUpRequest.getContrasena()));
        usuario.setNombre(signUpRequest.getNombre());
        usuario.setApellido(signUpRequest.getApellido());
        usuario.setTelefono(signUpRequest.getTelefono());
        
        // Asignar rol
        if ("admin".equalsIgnoreCase(signUpRequest.getNombreUsuario())) {
            usuario.setRol("ADMIN");
        } else {
            usuario.setRol("USER");
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente"));
    }

    // @PostMapping("/recuperar-contrasena")
    // public ResponseEntity<?> recuperarContrasena(@RequestParam String correo) {
    //     Usuario usuario = usuarioRepository.findByCorreo(correo)
    //         .orElse(null);

    //     if (usuario == null) {
    //         // Por seguridad, no revelamos si el correo existe o no
    //         return ResponseEntity.ok(new MessageResponse(
    //             "Si el correo existe en nuestro sistema, recibirás instrucciones para restablecer tu contraseña"
    //         ));
    //     }

    //     // Aquí implementarías la lógica de envío de correo
    //     // Por ahora solo retornamos un mensaje de éxito
    //     return ResponseEntity.ok(new MessageResponse(
    //         "Si el correo existe en nuestro sistema, recibirás instrucciones para restablecer tu contraseña"
    //     ));
    // }
}