import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { JwtResponse } from '../models/auth.models';
import { HttpClient } from '@angular/common/http';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('debería crear el servicio', () => {
    expect(service).toBeTruthy();
  });

  it('debería manejar el inicio de sesión correctamente', () => {
    const datosLogin = {
      correo: 'test@test.com',
      contrasena: '123456'
    };

    const respuestaMock: JwtResponse = {
      token: 'token-prueba',
      tipo: 'Bearer',
      id: 1,
      nombreUsuario: 'test',
      correo: 'test@test.com',
      rol: 'USER'
    };

    service.login(datosLogin).subscribe(respuesta => {
      expect(respuesta).toEqual(respuestaMock);
      expect(localStorage.getItem('auth_token')).toBe(respuestaMock.token);
    });

    const req = httpMock.expectOne(`${service['API_URL']}/login`);
    expect(req.request.method).toBe('POST');
    req.flush(respuestaMock);
  });

  it('debería manejar el cierre de sesión', () => {
    localStorage.setItem('auth_token', 'token-prueba');
    localStorage.setItem('user', JSON.stringify({ id: 1 }));

    service.logout();

    expect(localStorage.getItem('auth_token')).toBeNull();
    expect(localStorage.getItem('user')).toBeNull();
  });

 

  it('debería obtener el usuario del almacenamiento', () => {
    const usuarioMock = { id: 1, nombre: 'Test' };
    localStorage.setItem('user', JSON.stringify(usuarioMock));

    expect(service['getUserFromStorage']()).toEqual(usuarioMock);
  });

  it('debería manejar el registro de usuarios', () => {
    const usuarioNuevo = {
      nombreUsuario: 'test',
      correo: 'test@test.com',
      contrasena: '123456',
      nombre: 'Test',
      apellido: 'Usuario'
    };

    service.register(usuarioNuevo).subscribe(respuesta => {
      expect(respuesta).toBeTruthy();
    });

    const req = httpMock.expectOne(`${service['API_URL']}/registro`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('debería actualizar el subject del usuario al iniciar sesión', () => {
    const respuestaMock: JwtResponse = {
      token: 'token-prueba',
      tipo: 'Bearer',
      id: 1,
      nombreUsuario: 'test',
      correo: 'test@test.com',
      rol: 'USER'
    };

    service.login({ correo: 'test@test.com', contrasena: '123456' }).subscribe();

    const req = httpMock.expectOne(`${service['API_URL']}/login`);
    req.flush(respuestaMock);

    service.user$.subscribe(usuario => {
      expect(usuario).toEqual(respuestaMock);
    });
  });
});