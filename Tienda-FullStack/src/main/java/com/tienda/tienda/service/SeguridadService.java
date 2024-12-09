package com.tienda.tienda.service;

import com.tienda.tienda.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("seguridadService")
public class SeguridadService {
    
    public boolean esUsuarioActual(Long usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getId().equals(usuarioId);
    }
}