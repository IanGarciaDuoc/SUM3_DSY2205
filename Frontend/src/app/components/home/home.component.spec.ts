// home.component.spec.ts
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { HomeComponent } from './home.component';
import { AuthService } from '../../services/auth.service';
import { ProductoService } from '../../services/producto.service';
import { Router } from '@angular/router';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;
  let productoServiceSpy: jasmine.SpyObj<ProductoService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  const mockProductos = [
    {
      id: 1,
      nombre: 'Producto 1',
      descripcion: 'Descripción 1',
      precio: 100,
      stock: 10,
      activo: true,
      categoria: { id: 1, nombre: 'Smartphones', activo: true }
    },
    {
      id: 2,
      nombre: 'Producto 2',
      descripcion: 'Descripción 2',
      precio: 200,
      stock: 5,
      activo: true,
      categoria: { id: 2, nombre: 'Hogar', activo: true }
    }
  ];

  beforeEach(async () => {
    productoServiceSpy = jasmine.createSpyObj('ProductoService', [
      'getProductos',
      'searchProductos',
      'getProductosPorCategoria'
    ]);
    authServiceSpy = jasmine.createSpyObj('AuthService', [], {
      user$: of(null)
    });

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        FormsModule,
        HomeComponent
      ],
      providers: [
        { provide: ProductoService, useValue: productoServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar productos al inicializar', () => {
    productoServiceSpy.getProductos.and.returnValue(of(mockProductos));
    
    fixture.detectChanges();
    
    expect(productoServiceSpy.getProductos).toHaveBeenCalled();
    expect(component.productos).toEqual(mockProductos);
    expect(component.loading).toBeFalse();
  });

  it('debería manejar error al cargar productos', () => {
    productoServiceSpy.getProductos.and.returnValue(throwError(() => new Error('Error')));
    
    fixture.detectChanges();
    
    expect(component.error).toBe('Error al cargar los productos');
    expect(component.loading).toBeFalse();
  });

  it('debería buscar productos después de escribir en el buscador', fakeAsync(() => {
    const searchTerm = 'test';
    productoServiceSpy.searchProductos.and.returnValue(of(mockProductos));
    
    component.searchTerm = searchTerm;
    component.onSearch({ target: { value: searchTerm } });
    
    tick(300); // Esperar el debounceTime
    
    expect(productoServiceSpy.searchProductos).toHaveBeenCalledWith(searchTerm);
    expect(component.productos).toEqual(mockProductos);
  }));

  it('debería filtrar productos por categoría', () => {
    const categoriaId = 1;
    productoServiceSpy.getProductosPorCategoria.and.returnValue(of(mockProductos));
    
    component.filtrarPorCategoria(categoriaId);
    
    expect(productoServiceSpy.getProductosPorCategoria).toHaveBeenCalledWith(categoriaId);
    expect(component.productos).toEqual(mockProductos);
    expect(component.categoriaSeleccionada).toBe(categoriaId);
  });

  it('debería obtener todos los productos al deseleccionar categoría', () => {
    productoServiceSpy.getProductos.and.returnValue(of(mockProductos));
    
    component.filtrarPorCategoria(null);
    
    expect(productoServiceSpy.getProductos).toHaveBeenCalled();
    expect(component.categoriaSeleccionada).toBeNull();
  });

  it('debería navegar al detalle del producto', () => {
    const navigateSpy = spyOn(router, 'navigate');
    const productoId = 1;
    
    component.verDetalleProducto(productoId);
    
    expect(navigateSpy).toHaveBeenCalledWith(['/producto', productoId]);
  });



 
  


});
