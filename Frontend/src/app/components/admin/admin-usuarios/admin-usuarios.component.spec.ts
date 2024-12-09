import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AdminUsuariosComponent } from './admin-usuarios.component';
import { UsuarioService } from '../../../services/usuario.service';
import { of, throwError } from 'rxjs';
import { RouterModule, ActivatedRoute } from '@angular/router';

describe('AdminUsuariosComponent', () => {
 let component: AdminUsuariosComponent;
 let fixture: ComponentFixture<AdminUsuariosComponent>;
 let usuarioServiceSpy: jasmine.SpyObj<UsuarioService>;

 const mockUsuarios = [
   {
     id: 1,
     nombreUsuario: 'testuser',
     correo: 'test@test.com',
     nombre: 'Test',
     apellido: 'User',
     rol: 'ROLE_USER'
   }
 ];

 beforeEach(async () => {
   usuarioServiceSpy = jasmine.createSpyObj('UsuarioService', [
     'getUsuarios',
     'crearUsuario',
     'actualizarUsuario',
     'eliminarUsuario'
   ]);

   await TestBed.configureTestingModule({
     imports: [
       ReactiveFormsModule,
       RouterTestingModule,
       HttpClientTestingModule,
       RouterModule,
       AdminUsuariosComponent
     ],
     providers: [
       { provide: UsuarioService, useValue: usuarioServiceSpy },
       {
         provide: ActivatedRoute,
         useValue: {
           snapshot: { params: {}, queryParams: {} },
           params: of({})
         }
       }
     ]
   }).compileComponents();

   fixture = TestBed.createComponent(AdminUsuariosComponent);
   component = fixture.componentInstance;
 });

 it('debería crear el componente', () => {
   expect(component).toBeTruthy();
 });

 it('debería cargar usuarios al inicializar', () => {
   usuarioServiceSpy.getUsuarios.and.returnValue(of(mockUsuarios));
   
   fixture.detectChanges();
   
   expect(usuarioServiceSpy.getUsuarios).toHaveBeenCalled();
   expect(component.usuarios).toEqual(mockUsuarios);
   expect(component.loading).toBeFalse();
 });

 it('debería manejar error al cargar usuarios', () => {
   usuarioServiceSpy.getUsuarios.and.returnValue(throwError(() => new Error()));
   
   fixture.detectChanges();
   
   expect(component.error).toBe('Error al cargar los usuarios');
   expect(component.loading).toBeFalse();
 });

 it('debería validar formulario de usuario', () => {
   expect(component.usuarioForm.valid).toBeFalsy();
   
   component.usuarioForm.patchValue({
     nombreUsuario: 'testuser',
     correo: 'test@test.com',
     contrasena: 'Test123@',
     nombre: 'Test',
     apellido: 'User',
     rol: 'USER'
   });

   expect(component.usuarioForm.valid).toBeTruthy();
 });

 it('debería mostrar formulario de creación', () => {
   component.mostrarFormularioCreacion();
   
   expect(component.mostrandoFormulario).toBeTrue();
   expect(component.editandoUsuario).toBeFalse();
   expect(component.usuarioForm.get('rol')?.value).toBe('USER');
 });

 it('debería configurar formulario para edición', () => {
   const usuario = mockUsuarios[0];
   component.editarUsuario(usuario);
   
   expect(component.editandoUsuario).toBeTrue();
   expect(component.mostrandoFormulario).toBeTrue();
   expect(component.usuarioActualId).toBe(usuario.id);
   expect(component.usuarioForm.get('nombreUsuario')?.value).toBe(usuario.nombreUsuario);
 });

 it('debería crear un nuevo usuario', () => {
   const nuevoUsuario = {
     nombreUsuario: 'newuser',
     correo: 'new@test.com',
     contrasena: 'Test123@',
     nombre: 'New',
     apellido: 'User',
     rol: 'USER'
   };

   usuarioServiceSpy.crearUsuario.and.returnValue(of(mockUsuarios[0]));
   usuarioServiceSpy.getUsuarios.and.returnValue(of(mockUsuarios));

   component.usuarioForm.patchValue(nuevoUsuario);
   component.onSubmit();

   expect(usuarioServiceSpy.crearUsuario).toHaveBeenCalledWith({
     ...nuevoUsuario,
     rol: 'ROLE_USER'
   });
   expect(component.successMessage).toBe('Usuario creado exitosamente');
 });

 it('debería actualizar un usuario existente', () => {
   const usuarioActualizado = { ...mockUsuarios[0], nombre: 'Updated' };
   component.editarUsuario(mockUsuarios[0]);
   
   usuarioServiceSpy.actualizarUsuario.and.returnValue(of(usuarioActualizado));
   usuarioServiceSpy.getUsuarios.and.returnValue(of([usuarioActualizado]));

   component.usuarioForm.patchValue({
     nombre: 'Updated'
   });
   component.onSubmit();

   expect(usuarioServiceSpy.actualizarUsuario).toHaveBeenCalled();
   expect(component.successMessage).toBe('Usuario actualizado exitosamente');
 });

 it('debería eliminar un usuario', () => {
   spyOn(window, 'confirm').and.returnValue(true);
   usuarioServiceSpy.eliminarUsuario.and.returnValue(of(void 0));
   usuarioServiceSpy.getUsuarios.and.returnValue(of([]));

   component.eliminarUsuario(1);

   expect(usuarioServiceSpy.eliminarUsuario).toHaveBeenCalledWith(1);
 });

 it('debería cancelar la edición', () => {
   component.mostrandoFormulario = true;
   component.editandoUsuario = true;
   component.usuarioActualId = 1;

   component.cancelarEdicion();

   expect(component.mostrandoFormulario).toBeFalse();
   expect(component.editandoUsuario).toBeFalse();
   expect(component.usuarioActualId).toBeUndefined();
   expect(component.usuarioForm.get('rol')?.value).toBe('USER');
 });

 it('debería validar el formato de correo electrónico', () => {
   const correoControl = component.usuarioForm.get('correo');
   
   correoControl?.setValue('invalidEmail');
   expect(correoControl?.errors?.['email']).toBeTruthy();
   
   correoControl?.setValue('valid@email.com');
   expect(correoControl?.errors).toBeFalsy();
 });

 it('debería validar el patrón de contraseña', () => {
   const contrasenaControl = component.usuarioForm.get('contrasena');
   
   contrasenaControl?.setValue('weak');
   expect(contrasenaControl?.errors?.['pattern']).toBeTruthy();
   
   contrasenaControl?.setValue('StrongPass1@');
   expect(contrasenaControl?.errors).toBeFalsy();
 });

 it('debería mostrar mensaje de error si el usuario ya existe', () => {
   const usuario = {
     nombreUsuario: 'existinguser',
     correo: 'existing@test.com',
     contrasena: 'Test123@',
     nombre: 'Existing',
     apellido: 'User',
     rol: 'USER'
   };

   usuarioServiceSpy.crearUsuario.and.returnValue(
     throwError(() => ({ error: { message: 'El usuario ya existe' } }))
   );

   component.usuarioForm.patchValue(usuario);
   component.onSubmit();

   expect(component.error).toBe('El usuario ya existe');
 });

 it('debería limpiar contraseña al editar usuario', () => {
   component.editarUsuario(mockUsuarios[0]);
   
   const contrasenaControl = component.usuarioForm.get('contrasena');
   expect(contrasenaControl?.validator).toBeNull();
 });
});