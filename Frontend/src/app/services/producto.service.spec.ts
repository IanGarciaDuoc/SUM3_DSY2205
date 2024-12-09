// producto.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductoService } from './producto.service';
import { AuthService } from './auth.service';
import { Producto } from '../models/producto.model';

describe('ProductoService', () => {
  let service: ProductoService;
  let httpMock: HttpTestingController;
  let authServiceMock: jasmine.SpyObj<AuthService>;

  const mockProducto: Producto = {
    id: 1,
    nombre: 'Producto Test',
    descripcion: 'Descripción de prueba',
    precio: 100,
    stock: 10,
    activo: true,
    categoria: {
      id: 1,
      nombre: 'Categoría Test',
      activo: true
    }
  };

  beforeEach(() => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['login', 'logout']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        ProductoService,
        { provide: AuthService, useValue: authServiceMock }
      ]
    });

    service = TestBed.inject(ProductoService);
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

  it('debería obtener todos los productos', () => {
    const mockProductos: Producto[] = [mockProducto];

    service.getProductos().subscribe(productos => {
      expect(productos).toEqual(mockProductos);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/productos');
    expect(req.request.method).toBe('GET');
    expect(req.request.headers.get('Authorization')).toBe('Bearer fake-token');
    req.flush(mockProductos);
  });

  it('debería obtener un producto por ID', () => {
    service.getProducto(1).subscribe(producto => {
      expect(producto).toEqual(mockProducto);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/productos/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockProducto);
  });

  

  it('debería actualizar un producto existente', () => {
    const productoActualizado = { ...mockProducto, nombre: 'Actualizado' };

    service.actualizarProducto(1, productoActualizado).subscribe(response => {
      expect(response).toEqual(productoActualizado);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/productos/1');
    expect(req.request.method).toBe('PUT');
    req.flush(productoActualizado);
  });

  it('debería eliminar un producto', () => {
    service.eliminarProducto(1).subscribe();

    const req = httpMock.expectOne('http://localhost:8080/api/productos/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('debería buscar productos', () => {
    const query = 'test';
    const mockResultados = [mockProducto];

    service.searchProductos(query).subscribe(productos => {
      expect(productos).toEqual(mockResultados);
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/productos/search?query=${query}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResultados);
  });

  it('debería obtener productos por categoría', () => {
    const categoriaId = 1;
    const mockProductosCategoria = [mockProducto];

    service.getProductosPorCategoria(categoriaId).subscribe(productos => {
      expect(productos).toEqual(mockProductosCategoria);
    });

    const req = httpMock.expectOne(`http://localhost:8080/api/productos/categoria/${categoriaId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockProductosCategoria);
  });

  it('debería manejar error cuando no hay token', () => {
    localStorage.removeItem('auth_token');
    spyOn(console, 'error');

    service.getProductos().subscribe();

    const req = httpMock.expectOne('http://localhost:8080/api/productos');
    expect(console.error).toHaveBeenCalledWith('Token no encontrado en localStorage');
    req.flush(null);
  });

  it('debería incluir headers correctos en las peticiones', () => {
    const headers = service['getHeaders']();
    
    expect(headers.get('Content-Type')).toBe('application/json');
    expect(headers.get('Authorization')).toBe('Bearer fake-token');
    expect(headers.get('Accept')).toBe('application/json');
  });
});