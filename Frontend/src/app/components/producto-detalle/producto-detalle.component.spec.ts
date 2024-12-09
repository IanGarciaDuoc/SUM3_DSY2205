// producto-detalle.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { ProductoDetalleComponent } from './producto-detalle.component';
import { ProductoService } from '../../services/producto.service';
import { NavbarComponent } from '../navbar/navbar.component';

describe('ProductoDetalleComponent', () => {
  let component: ProductoDetalleComponent;
  let fixture: ComponentFixture<ProductoDetalleComponent>;
  let productoServiceSpy: jasmine.SpyObj<ProductoService>;

  const mockCategoria = {
    id: 1,
    nombre: 'Categoria Test',
    descripcion: 'Descripción categoria',
    activo: true,
    fechaCreacion: '2024-01-01',
    fechaActualizacion: '2024-01-02'
  };

  const mockProducto = {
    id: 1,
    nombre: 'Producto Test',
    descripcion: 'Descripción de prueba',
    precio: 100,
    stock: 10,
    urlImagen: 'test.jpg',
    activo: true,
    fechaCreacion: '2024-01-01',
    fechaActualizacion: '2024-01-02',
    categoria: mockCategoria
  };

  beforeEach(async () => {
    productoServiceSpy = jasmine.createSpyObj('ProductoService', ['getProducto']);

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        ProductoDetalleComponent,
        NavbarComponent
      ],
      providers: [
        { provide: ProductoService, useValue: productoServiceSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' })
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductoDetalleComponent);
    component = fixture.componentInstance;
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar el producto correctamente', () => {
    productoServiceSpy.getProducto.and.returnValue(of(mockProducto));
    component.ngOnInit();
    
    fixture.detectChanges();

    expect(component.producto).toEqual(mockProducto);
    expect(component.loading).toBeFalse();
  });

  it('debería manejar error al cargar el producto', () => {
    productoServiceSpy.getProducto.and.returnValue(throwError(() => new Error()));
    component.ngOnInit();
    
    fixture.detectChanges();

    expect(component.error).toBe('Error al cargar el producto');
    expect(component.loading).toBeFalse();
  });

  it('debería formatear la fecha correctamente', () => {
    expect(component.formatearFecha(undefined)).toBe('No disponible');
    const fecha = '2024-01-01';
    expect(component.formatearFecha(fecha)).toBe(new Date(fecha).toLocaleDateString());
  });

  it('debería llamar a window.history.back() al ejecutar volverAtras()', () => {
    spyOn(window.history, 'back');
    component.volverAtras();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('debería mostrar spinner mientras loading es true', () => {
    component.loading = true;
    fixture.detectChanges();
    const spinner = fixture.nativeElement.querySelector('.animate-spin');
    expect(spinner).toBeTruthy();
  });

 

  it('debería mostrar mensaje de error cuando existe', () => {
    component.error = 'Error de prueba';
    component.loading = false;
    fixture.detectChanges();
    
    const errorMsg = fixture.nativeElement.querySelector('.bg-red-100');
    expect(errorMsg.textContent).toContain('Error de prueba');
  });
});