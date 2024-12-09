/**
 * Interfaz para la solicitud de inicio de sesión
 */
export interface LoginRequest {
    /** Correo electrónico del usuario */
    correo: string;
    /** Contraseña del usuario */
    contrasena: string;
  }
  
  /**
   * Interfaz para la respuesta del servidor después de un inicio de sesión exitoso
   */
  // src/app/models/auth.interface.ts
export interface LoginResponse {
    token: string;
    tipo: string;
    id: number;
    nombreUsuario: string;
    correo: string;
    rol: string;
  }
  
  /**
   * Interfaz para la solicitud de registro de usuario
   */
  export interface RegisterRequest {
    /** Nombre de usuario único */
    nombreUsuario: string;
    /** Correo electrónico único */
    correo: string;
    /** Contraseña que cumple con los requisitos de seguridad */
    contrasena: string;
    /** Nombre real del usuario */
    nombre: string;
    /** Apellido del usuario */
    apellido: string;
    /** Número de teléfono del usuario */
    telefono: string;
  }
  
  /**
   * Interfaz para mensajes de respuesta genéricos del servidor
   */
  export interface MessageResponse {
    /** Mensaje informativo o de error */
    message: string;
    /** Código de estado opcional */
    statusCode?: number;
    /** Tipo de mensaje opcional */
    type?: 'success' | 'error' | 'warning' | 'info'}