import { Categoria } from './categoria.model'
export interface Producto {
  id: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  urlImagen?: string;
  activo: boolean;
  categoria?: Categoria;
  fechaCreacion?: string;
  fechaActualizacion?: string;
}