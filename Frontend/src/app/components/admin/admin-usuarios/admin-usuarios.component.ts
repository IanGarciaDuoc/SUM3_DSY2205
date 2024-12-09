import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http'; 
import { UsuarioService } from '../../../services/usuario.service';
import { Usuario } from '../../../models/usuario.model';
import { NavbarComponent } from '../../navbar/navbar.component'; 

@Component({
    selector: 'app-admin-usuarios',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule,NavbarComponent,HttpClientModule],
    templateUrl: './admin-usuarios.component.html'
  })
  export class AdminUsuariosComponent implements OnInit {
    usuarios: Usuario[] = [];
    usuarioForm: FormGroup;
    editandoUsuario: boolean = false;
    mostrandoFormulario: boolean = false;
    usuarioActualId?: number;
    loading: boolean = false;
    error: string = '';
    successMessage: string = '';
  
    constructor(
      private fb: FormBuilder,
      private usuarioService: UsuarioService
    ) {
      this.usuarioForm = this.fb.group({
        nombreUsuario: ['', [Validators.required, Validators.minLength(3)]],
        correo: ['', [Validators.required, Validators.email]],
        contrasena: ['', [Validators.required, Validators.pattern(/^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\S+$).{8,20}$/)]],
        nombre: ['', Validators.required],
        apellido: ['', Validators.required],
        rol: ['USER', Validators.required]
      });
    }
  
    ngOnInit() {
      this.cargarUsuarios();
    }
  
    cargarUsuarios() {
      this.loading = true;
      this.usuarioService.getUsuarios().subscribe({
        next: (usuarios) => {
          this.usuarios = usuarios;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error al cargar los usuarios';
          this.loading = false;
        }
      });
    }
  
    mostrarFormularioCreacion() {
      this.mostrandoFormulario = true;
      this.editandoUsuario = false;
      this.usuarioForm.reset({rol: 'USER'});
    }
  
    editarUsuario(usuario: Usuario) {
      this.editandoUsuario = true;
      this.mostrandoFormulario = true;
      this.usuarioActualId = usuario.id;
      
      this.usuarioForm.patchValue({
        nombreUsuario: usuario.nombreUsuario,
        correo: usuario.correo,
        nombre: usuario.nombre,
        apellido: usuario.apellido,
        rol: usuario.rol
      });
      this.usuarioForm.get('contrasena')?.setValidators(null);
      this.usuarioForm.get('contrasena')?.updateValueAndValidity();
    }
  
    eliminarUsuario(id: number) {
      if (confirm('¿Está seguro de eliminar este usuario?')) {
        this.usuarioService.eliminarUsuario(id).subscribe({
          next: () => {
            this.cargarUsuarios();
          },
          error: (err) => {
            this.error = 'Error al eliminar el usuario';
          }
        });
      }
    }
  
    onSubmit() {
      if (this.usuarioForm.valid) {
        this.loading = true;
        this.error = '';
    
        // Asegurarse que el rol tenga el formato correcto
        const formData = this.usuarioForm.value;
        const usuario = {
          nombreUsuario: formData.nombreUsuario,
          correo: formData.correo,
          contrasena: formData.contrasena,
          nombre: formData.nombre,
          apellido: formData.apellido,
          rol: formData.rol.startsWith('ROLE_') ? formData.rol : `ROLE_${formData.rol}`
        };
    
        console.log('Enviando usuario:', usuario);
    
        if (this.editandoUsuario && this.usuarioActualId) {
          this.usuarioService.actualizarUsuario(this.usuarioActualId, usuario)
            .subscribe({
              next: (response) => {
                console.log('Usuario actualizado:', response);
                this.successMessage = 'Usuario actualizado exitosamente';
                this.cargarUsuarios();
                this.cancelarEdicion();
              },
              error: (err) => {
                console.error('Error:', err);
                this.error = err.error?.message || 'Error al actualizar usuario';
              },
              complete: () => {
                this.loading = false;
              }
            });
        } else {
          this.usuarioService.crearUsuario(usuario)
            .subscribe({
              next: (response) => {
                console.log('Usuario creado:', response);
                this.successMessage = 'Usuario creado exitosamente';
                this.cargarUsuarios();
                this.cancelarEdicion();
              },
              error: (err) => {
                console.error('Error:', err);
                this.error = err.error?.message || 'Error al crear usuario';
              },
              complete: () => {
                this.loading = false;
              }
            });
        }
      }
    }
    cancelarEdicion() {
      this.mostrandoFormulario = false;
      this.editandoUsuario = false;
      this.usuarioActualId = undefined;
      this.usuarioForm.reset({rol: 'USER'});
    }

    private verificarToken() {
      const token = localStorage.getItem('auth_token');
      if (!token) {
        this.error = 'No hay token de autenticación. Por favor, inicie sesión nuevamente.';
        // Redirigir al login si es necesario
        return false;
      }
      return true;
    }

  }