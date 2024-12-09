// src/app/components/producto-detalle/producto-detalle.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-producto-detalle',
  standalone: true,
  imports: [CommonModule ,NavbarComponent] ,
  templateUrl: './producto-detalle.component.html'
})
export class ProductoDetalleComponent implements OnInit {
  producto: Producto | null = null;
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productoService: ProductoService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = +params['id'];
      this.cargarProducto(id);
    });
  }

  cargarProducto(id: number) {
    this.loading = true;
    this.productoService.getProducto(id).subscribe({
      next: (data: Producto) => {
        this.producto = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar el producto:', error);
        this.error = 'Error al cargar el producto';
        this.loading = false;
      }
    });
  }

  volverAtras() {
    window.history.back();
  }

  formatearFecha(fecha: string | undefined): string {
    if (!fecha) return 'No disponible';
    return new Date(fecha).toLocaleDateString();
  }
}