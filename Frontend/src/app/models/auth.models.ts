export interface LoginRequest {
    correo: string;
    contrasena: string;
  }
  
  export interface JwtResponse {
    token: string;
    tipo: string;
    id: number;
    nombreUsuario: string;
    correo: string;
    rol: string;
  }