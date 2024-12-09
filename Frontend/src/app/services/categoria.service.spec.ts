// categoria.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CategoriaService } from './categoria.services';
import { Categoria } from '../models/categoria.model';

describe('CategoriaService', () => {
  let service: CategoriaService;
  let httpMock: HttpTestingController;

  const mockCategorias: Categoria[] = [
    {
      id: 1,
      nombre: 'Electrónicos',
      descripcion: 'Productos electrónicos',
      activo: true,
      fechaCreacion: '2024-01-01',
      fechaActualizacion: '2024-01-01'
    },
    {
      id: 2,
      nombre: 'Ropa',
      descripcion: 'Productos de vestir',
      activo: true,
      fechaCreacion: '2024-01-01',
      fechaActualizacion: '2024-01-01'
    }
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CategoriaService]
    });

    service = TestBed.inject(CategoriaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('debería crear el servicio', () => {
    expect(service).toBeTruthy();
  });

  it('debería obtener las categorías con headers correctos', () => {
    const token = 'fake-token';
    localStorage.setItem('auth_token', token);

    service.getCategorias().subscribe(categorias => {
      expect(categorias).toEqual(mockCategorias);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/categorias');
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe(`Bearer ${token}`);
    expect(req.request.headers.get('Content-Type')).toBe('application/json');
    
    req.flush(mockCategorias);
  });

  it('debería manejar el caso de no tener token', () => {
    localStorage.removeItem('auth_token');

    service.getCategorias().subscribe(categorias => {
      expect(categorias).toEqual(mockCategorias);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/categorias');
    expect(req.request.headers.get('Authorization')).toBe('Bearer null');
    
    req.flush(mockCategorias);
  });

  it('debería obtener headers con el token correcto', () => {
    const token = 'test-token';
    localStorage.setItem('auth_token', token);

    const headers = service['getHeaders']();

    expect(headers.get('Content-Type')).toBe('application/json');
    expect(headers.get('Authorization')).toBe(`Bearer ${token}`);
  });

  it('debería manejar error al obtener categorías', () => {
    service.getCategorias().subscribe({
      error: (error) => {
        expect(error.status).toBe(401);
      }
    });

    const req = httpMock.expectOne('http://localhost:8080/api/categorias');
    req.error(new ErrorEvent('Unauthorized'), { status: 401 });
  });
});