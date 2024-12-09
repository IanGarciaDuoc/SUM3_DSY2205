import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    // Crear mock para AuthService
    authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería inicializar el formulario de registro con campos vacíos', () => {
    const form = component.registerForm;
    expect(form).toBeTruthy();
    expect(form.get('nombreUsuario')?.value).toBe('');
    expect(form.get('correo')?.value).toBe('');
    expect(form.get('contrasena')?.value).toBe('');
    expect(form.get('nombre')?.value).toBe('');
    expect(form.get('apellido')?.value).toBe('');
    expect(form.get('telefono')?.value).toBe('');
  });

  it('debería mostrar errores de validación para campos requeridos', () => {
    const form = component.registerForm;
    form.get('nombreUsuario')?.setValue('');
    form.get('correo')?.setValue('');
    form.get('contrasena')?.setValue('');
    form.get('nombre')?.setValue('');
    form.get('apellido')?.setValue('');

    form.markAllAsTouched();
    fixture.detectChanges();

    expect(form.get('nombreUsuario')?.errors?.['required']).toBeTruthy();
    expect(form.get('correo')?.errors?.['required']).toBeTruthy();
    expect(form.get('contrasena')?.errors?.['required']).toBeTruthy();
    expect(form.get('nombre')?.errors?.['required']).toBeTruthy();
    expect(form.get('apellido')?.errors?.['required']).toBeTruthy();
  });

  it('debería mostrar un error si la contraseña no cumple con el patrón', () => {
    const form = component.registerForm;
    form.get('contrasena')?.setValue('simple123');
    form.get('contrasena')?.markAsTouched();

    fixture.detectChanges();

    expect(form.get('contrasena')?.errors?.['pattern']).toBeTruthy();
  });

  it('debería llamar a AuthService.register con datos válidos', () => {
    const validData = {
      nombreUsuario: 'usuarioTest',
      correo: 'test@example.com',
      contrasena: 'Password123@',
      nombre: 'Test',
      apellido: 'Usuario',
      telefono: '123456789',
    };

    authServiceSpy.register.and.returnValue(of({}));

    component.registerForm.setValue(validData);
    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalledWith(validData);
  });

  it('debería mostrar un mensaje de error si el registro falla', () => {
    const invalidData = {
      nombreUsuario: 'usuarioTest',
      correo: 'test@example.com',
      contrasena: 'Password123@',
      nombre: 'Test',
      apellido: 'Usuario',
      telefono: '123456789',
    };

    authServiceSpy.register.and.returnValue(
      throwError({ error: { mensaje: 'Correo ya registrado' } })
    );

    component.registerForm.setValue(invalidData);
    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalledWith(invalidData);
    expect(component.error).toBe('Correo ya registrado');
  });

  it('debería redirigir al login después de un registro exitoso', () => {
    const validData = {
      nombreUsuario: 'usuarioTest',
      correo: 'test@example.com',
      contrasena: 'Password123@',
      nombre: 'Test',
      apellido: 'Usuario',
      telefono: '123456789',
    };

    authServiceSpy.register.and.returnValue(of({}));
    const navigateSpy = spyOn(component['router'], 'navigate');

    component.registerForm.setValue(validData);
    component.onSubmit();

    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
  });
});
