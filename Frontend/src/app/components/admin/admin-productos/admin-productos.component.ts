import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductoService } from '../../../services/producto.service';
import { Producto } from '../../../models/producto.model';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { Categoria } from '../../../models/categoria.model';
import { NavbarComponent } from '../../navbar/navbar.component'; 

@Component({
  selector: 'app-admin-productos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,NavbarComponent],
  templateUrl: './admin-productos.component.html'
})
export class AdminProductosComponent implements OnInit {
  productos: Producto[] = [];
  productoForm: FormGroup;
  editandoProducto: boolean = false;
  productoActualId?: number;
  loading: boolean = false;
  error: string = '';
  successMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private productoService: ProductoService,
    private authService: AuthService,
    private router: Router
  ) {
    this.productoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      descripcion: ['', Validators.required],
      precio: ['', [Validators.required, Validators.min(0)]],
      stock: ['', [Validators.required, Validators.min(0)]],
      urlImagen: ['', Validators.required],
      categoriaId: ['']
    });
  }

  ngOnInit() {
    this.cargarProductos();
  }

  cargarProductos() {
    this.loading = true;
    this.error = '';
    this.productoService.getProductos().subscribe({
      next: (productos) => {
        this.productos = productos;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Error al cargar los productos';
        this.loading = false;
        console.error('Error:', err);
      }
    });
  }

  onSubmit() {
    if (this.productoForm.valid) {
      this.loading = true;
      this.error = '';
      
      // Convertir los valores numéricos apropiadamente
      const productoData = {
        ...this.productoForm.value,
        precio: Number(this.productoForm.get('precio')?.value),
        stock: Number(this.productoForm.get('stock')?.value),
        activo: true
      };
  
      console.log('Enviando producto:', productoData); // Para debug
  
      if (this.editandoProducto && this.productoActualId) {
        this.productoService.actualizarProducto(this.productoActualId, productoData)
          .subscribe({
            next: (response) => {
              console.log('Respuesta:', response);
              this.successMessage = 'Producto actualizado exitosamente';
              this.cargarProductos();
              this.resetForm();
            },
            error: (err) => {
              console.error('Error completo:', err);
              this.error = err.error?.message || 'Error al actualizar el producto';
            },
            complete: () => {
              this.loading = false;
            }
          });
      } else {
        this.productoService.crearProducto(productoData)
          .subscribe({
            next: (response) => {
              console.log('Respuesta:', response);
              this.successMessage = 'Producto creado exitosamente';
              this.cargarProductos();
              this.resetForm();
            },
            error: (err) => {
              console.error('Error completo:', err);
              this.error = err.error?.message || 'Error al crear el producto';
            },
            complete: () => {
              this.loading = false;
            }
          });
      }
    } else {
      this.error = 'Por favor, complete todos los campos requeridos correctamente';
    }
  }

  editarProducto(producto: Producto) {
    this.editandoProducto = true;
    this.productoActualId = producto.id;
    this.productoForm.patchValue({
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      precio: producto.precio,
      stock: producto.stock,
      urlImagen: producto.urlImagen,
      categoriaId: producto.categoria
    });
  }

  eliminarProducto(id: number) {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.loading = true;
      this.error = '';
      
      this.productoService.eliminarProducto(id).subscribe({
        next: () => {
          this.successMessage = 'Producto eliminado exitosamente';
          this.cargarProductos();
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error al eliminar el producto';
          this.loading = false;
          console.error('Error:', err);
        }
      });
    }
  }

  resetForm() {
    this.productoForm.reset();
    this.editandoProducto = false;
    this.productoActualId = undefined;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}