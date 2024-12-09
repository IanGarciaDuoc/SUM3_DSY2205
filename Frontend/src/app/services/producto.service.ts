import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Producto } from '../models/producto.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProductoService {
  private API_URL = 'http://localhost:8080/api/productos';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  // Método para obtener el token desde localStorage y construir los headers
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token'); // Obtenemos el token de localStorage
    console.log("Token en encabezado:", token);
    if (!token) {
      console.error("Token no encontrado en localStorage"); // Mensaje de error si el token es nulo
    }
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json'
    });
  }

  // Obtener todos los productos
  getProductos(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.API_URL, { headers: this.getHeaders() });
  }

  // Obtener un producto por ID
  getProducto(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }

  // Crear un nuevo producto
  crearProducto(producto: Producto): Observable<any> {
    console.log('Datos a enviar:', producto); // Para debug
    return this.http.post(this.API_URL, producto, { 
      headers: this.getHeaders(),
      observe: 'response'  // Para ver la respuesta completa
    });
  }

  // Actualizar un producto existente
  actualizarProducto(id: number, producto: Producto): Observable<any> {
    return this.http.put(`${this.API_URL}/${id}`, producto, { headers: this.getHeaders() });
  }

  // Eliminar un producto
  eliminarProducto(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
  }
  searchProductos(query: string): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.API_URL}/search?query=${query}`, 
      { headers: this.getHeaders() }
    );
  }
  getProductosPorCategoria(categoriaId: number): Observable<Producto[]> {
    return this.http.get<Producto[]>(`${this.API_URL}/categoria/${categoriaId}`, 
      { headers: this.getHeaders() }
    );
  }
}
