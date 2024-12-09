// usuario.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UsuarioService } from './usuario.service';
import { Usuario } from '../models/usuario.model';

describe('UsuarioService', () => {
  let service: UsuarioService;
  let httpMock: HttpTestingController;

  const mockUsuario: Usuario = {
    id: 1,
    nombreUsuario: 'testuser',
    correo: 'test@test.com',
    rol: 'USER',
    nombre: 'Test',
    apellido: 'Usuario',
    telefono: '123456789',
    contrasena: 'password123'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UsuarioService]
    });

    service = TestBed.inject(UsuarioService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.setItem('auth_token', 'fake-token');
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('debería crear el servicio', () => {
    expect(service).toBeTruthy();
  });

  it('debería obtener todos los usuarios', () => {
    const mockUsuarios: Usuario[] = [mockUsuario];

    service.getUsuarios().subscribe(usuarios => {
      expect(usuarios).toEqual(mockUsuarios);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios');
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer fake-token');
    req.flush(mockUsuarios);
  });

  it('debería obtener un usuario por ID', () => {
    service.getUsuario(1).subscribe(usuario => {
      expect(usuario).toEqual(mockUsuario);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUsuario);
  });

  it('debería crear un nuevo usuario', () => {
    const nuevoUsuario = { ...mockUsuario, id: undefined };

    service.crearUsuario(nuevoUsuario).subscribe(response => {
      expect(response).toEqual(mockUsuario);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(nuevoUsuario);
    req.flush(mockUsuario);
  });

  it('debería actualizar un usuario existente', () => {
    const usuarioActualizado: Partial<Usuario> = {
      nombre: 'Actualizado',
      apellido: 'Test'
    };

    service.actualizarUsuario(1, usuarioActualizado).subscribe(response => {
      expect(response).toEqual({ ...mockUsuario, ...usuarioActualizado });
    });

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios/1');
    expect(req.request.method).toBe('PUT');
    req.flush({ ...mockUsuario, ...usuarioActualizado });
  });

  it('debería eliminar un usuario', () => {
    service.eliminarUsuario(1).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('debería manejar error cuando no hay token', () => {
    localStorage.removeItem('auth_token');
    spyOn(console, 'error');

    service.getUsuarios().subscribe();

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios');
    expect(console.error).toHaveBeenCalledWith('No se encontró token de autenticación');
    req.flush(null);
  });

  it('debería incluir headers correctos en las peticiones', () => {
    const headers = service['getHeaders']();
    
    expect(headers.get('Content-Type')).toBe('application/json');
    expect(headers.get('Authorization')).toBe('Bearer fake-token');
    expect(headers.get('Accept')).toBe('application/json');
  });

  it('debería manejar errores en las peticiones', () => {
    service.getUsuarios().subscribe({
      error: error => {
        expect(error.status).toBe(401);
      }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/usuarios');
    req.error(new ErrorEvent('Error'), { status: 401 });
  });

  
});