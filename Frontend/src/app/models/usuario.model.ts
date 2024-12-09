// usuario.model.ts
export interface Usuario {
    id?: number;
    nombreUsuario: string;
    correo: string;
    contrasena?: string; // Hacerlo opcional si no se necesita en algunas vistas
    rol: string;
    nombre: string;
    apellido?: string;
    telefono?: string;
    fechaCreacion?: Date;
    fechaActualizacion?: Date;
  }
  