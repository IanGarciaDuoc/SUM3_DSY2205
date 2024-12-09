// admin-productos.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AdminProductosComponent } from './admin-productos.component';
import { ProductoService } from '../../../services/producto.service';
import { AuthService } from '../../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('AdminProductosComponent', () => {
  let component: AdminProductosComponent;
  let fixture: ComponentFixture<AdminProductosComponent>;
  let productoServiceSpy: jasmine.SpyObj<ProductoService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  const mockProductos = [
    {
      id: 1,
      nombre: 'Producto Test',
      descripcion: 'Descripción test',
      precio: 100,
      stock: 10,
      urlImagen: 'test.jpg',
      activo: true,
      categoria: { id: 1, nombre: 'Categoría Test', activo: true }
    }
  ];

  beforeEach(async () => {
    productoServiceSpy = jasmine.createSpyObj('ProductoService', [
      'getProductos',
      'crearProducto',
      'actualizarProducto',
      'eliminarProducto'
    ]);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        RouterTestingModule,
        HttpClientTestingModule,
        AdminProductosComponent
      ],
      providers: [
        { provide: ProductoService, useValue: productoServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminProductosComponent);
    component = fixture.componentInstance;
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

 

  

  it('debería validar formulario de producto', () => {
    expect(component.productoForm.valid).toBeFalsy();
    
    component.productoForm.patchValue({
      nombre: 'Test',
      descripcion: 'Descripción',
      precio: 100,
      stock: 10,
      urlImagen: 'test.jpg',
      categoriaId: 1
    });

    expect(component.productoForm.valid).toBeTruthy();
  });



  it('debería actualizar un producto existente', () => {
    const productoActualizado = { ...mockProductos[0], nombre: 'Actualizado' };
    component.editarProducto(mockProductos[0]);
    
    productoServiceSpy.actualizarProducto.and.returnValue(of(productoActualizado));
    productoServiceSpy.getProductos.and.returnValue(of([productoActualizado]));

    component.productoForm.patchValue({
      nombre: 'Actualizado'
    });
    component.onSubmit();

    expect(productoServiceSpy.actualizarProducto).toHaveBeenCalled();
    expect(component.successMessage).toBe('Producto actualizado exitosamente');
  });

  it('debería eliminar un producto', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    productoServiceSpy.eliminarProducto.and.returnValue(of(void 0));
    productoServiceSpy.getProductos.and.returnValue(of([]));

    component.eliminarProducto(1);

    expect(productoServiceSpy.eliminarProducto).toHaveBeenCalledWith(1);
    expect(component.successMessage).toBe('Producto eliminado exitosamente');
  });

  it('debería manejar error al crear producto', () => {
    productoServiceSpy.crearProducto.and.returnValue(throwError(() => ({ error: { message: 'Error test' } })));

    component.productoForm.patchValue({
      nombre: 'Test',
      descripcion: 'Descripción',
      precio: 100,
      stock: 10,
      urlImagen: 'test.jpg',
      categoriaId: 1
    });
    component.onSubmit();

    expect(component.error).toBe('Error test');
  });

  it('debería resetear el formulario', () => {
    component.productoForm.patchValue({
      nombre: 'Test'
    });
    component.editandoProducto = true;
    component.productoActualId = 1;

    component.resetForm();

    expect(component.productoForm.pristine).toBeTrue();
    expect(component.editandoProducto).toBeFalse();
    expect(component.productoActualId).toBeUndefined();
  });


});