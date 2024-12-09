import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { LoginRequest, JwtResponse } from '../models/auth.models';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API_URL = 'http://localhost:8080/api/auth';
  private userSubject = new BehaviorSubject<any>(this.getUserFromStorage());

  constructor(private http: HttpClient) { }

  private getUserFromStorage() {
    const token = localStorage.getItem('auth_token');
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  get user$() {
    return this.userSubject.asObservable();
  }

  get isLoggedIn(): boolean {
    return !!this.userSubject.value;
  }

  login(credentials: LoginRequest): Observable<JwtResponse> {
    return this.http.post<JwtResponse>(`${this.API_URL}/login`, credentials)
      .pipe(tap(response => {
        localStorage.setItem('auth_token', response.token);
        localStorage.setItem('user', JSON.stringify({
          id: response.id,
          nombreUsuario: response.nombreUsuario,
          correo: response.correo,
          rol: response.rol
        }));
        this.userSubject.next(response);
      }));
  }

  // Agregar este método
  register(userData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/registro`, userData);
  }

  logout(): void {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('user');
    this.userSubject.next(null);
  }
}