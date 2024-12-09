import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ProductoService } from '../../services/producto.service';
import { NavbarComponent } from '../navbar/navbar.component';
import { Producto } from '../../models/producto.model';
import { Categoria } from '../../models/categoria.model';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, NavbarComponent],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  productos: Producto[] = [];
  searchTerm: string = '';
  loading: boolean = false;
  error: string | null = null;
  private searchSubject = new Subject<string>();
  categoriaSeleccionada: number | null = null;

  categorias: Categoria[] = [
    { id: 1, nombre: 'Smartphones', activo: true },
    { id: 2, nombre: 'Hogar', activo: true },
    { id: 3, nombre: 'Moda', activo: true },
    { id: 4, nombre: 'Alimentos', activo: true },
    { id: 5, nombre: 'Deportes', activo: true }
  ];

  constructor(
    public authService: AuthService,
    private productoService: ProductoService,
    private router: Router
  ) {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(term => {
      if (term) {
        this.buscarProductos(term);
      } else {
        this.obtenerProductos();
      }
    });
  }

  ngOnInit() {
    this.obtenerProductos();
    this.authService.user$.subscribe(user => {
      console.log('Usuario actual:', user);
      console.log('Rol:', user?.rol);
    });
  }

  obtenerProductos() {
    this.loading = true;
    this.error = null;
    this.productoService.getProductos().subscribe({
      next: (data: Producto[]) => {
        this.productos = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al obtener los productos:', err);
        this.error = 'Error al cargar los productos';
        this.loading = false;
      }
    });
  }

  onSearch(event: any) {
    this.searchSubject.next(event.target.value);
  }

  buscarProductos(term: string) {
    this.loading = true;
    this.error = null;
    this.productoService.searchProductos(term).subscribe({
      next: (data: Producto[]) => {
        this.productos = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al buscar productos:', err);
        this.error = 'Error en la búsqueda';
        this.loading = false;
      }
    });
  }

  filtrarPorCategoria(categoriaId: number | null) {
    this.categoriaSeleccionada = categoriaId;
    this.loading = true;
    this.error = null;

    if (categoriaId) {
      this.productoService.getProductosPorCategoria(categoriaId).subscribe({
        next: (productos) => {
          this.productos = productos;
          this.loading = false;
        },
        error: (err) => {
          console.error('Error al filtrar productos:', err);
          this.error = 'Error al filtrar los productos';
          this.loading = false;
        }
      });
    } else {
      this.obtenerProductos();
    }
  }

  verDetalleProducto(id: number | undefined) {
    if (id) {
      this.router.navigate(['/producto', id]);
    } else {
      console.error('ID de producto no válido');
    }
  }
}