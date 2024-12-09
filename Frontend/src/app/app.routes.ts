import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AdminProductosComponent } from './components/admin/admin-productos/admin-productos.component';
import { AdminUsuariosComponent } from './components/admin/admin-usuarios/admin-usuarios.component';
import { ProductoDetalleComponent } from './components/producto-detalle/producto-detalle.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'admin/productos', component: AdminProductosComponent},
  { path: 'admin/usuarios', component: AdminUsuariosComponent },
  { path: 'producto/:id', component: ProductoDetalleComponent },

];