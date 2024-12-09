import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';

@Injectable({
    providedIn: 'root'
  })
  export class UsuarioService {
    private API_URL = 'http://localhost:8080/api/usuarios';
  
    constructor(private http: HttpClient) { }
  
    private getHeaders(): HttpHeaders {
      const token = localStorage.getItem('auth_token');
      if (!token) {
        console.error('No se encontró token de autenticación');
      }
      console.log('Token:', token); // Para debug
      
      return new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        'Accept': 'application/json'
      });
    }
    getUsuarios(): Observable<Usuario[]> {
      return this.http.get<Usuario[]>(this.API_URL, { headers: this.getHeaders() });
    }
  
    getUsuario(id: number): Observable<Usuario> {
      return this.http.get<Usuario>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
    }
  
    crearUsuario(usuario: Usuario): Observable<Usuario> {
      return this.http.post<Usuario>(this.API_URL, usuario, { headers: this.getHeaders() });
    }
  
    actualizarUsuario(id: number, usuario: Partial<Usuario>): Observable<Usuario> {
      return this.http.put<Usuario>(`${this.API_URL}/${id}`, usuario, { headers: this.getHeaders() });
    }
  
    eliminarUsuario(id: number): Observable<void> {
      return this.http.delete<void>(`${this.API_URL}/${id}`, { headers: this.getHeaders() });
    }
  }