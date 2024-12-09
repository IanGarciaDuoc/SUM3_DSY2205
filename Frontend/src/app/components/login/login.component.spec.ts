import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing'; // Importa el módulo de pruebas de enrutamiento
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { LoginComponent } from './login.component';
describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    // Crear mock para AuthService
    authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule], // Agrega RouterTestingModule
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería inicializar el formulario de inicio de sesión con campos vacíos', () => {
    const loginForm = component.loginForm;
    expect(loginForm).toBeTruthy();
    expect(loginForm.get('correo')?.value).toBe('');
    expect(loginForm.get('contrasena')?.value).toBe('');
  });

  it('debería mostrar errores de validación para un correo y contraseña inválidos', () => {
    const emailInput = component.loginForm.get('correo');
    const passwordInput = component.loginForm.get('contrasena');

    emailInput?.setValue('');
    passwordInput?.setValue('');
    emailInput?.markAsTouched();
    passwordInput?.markAsTouched();

    fixture.detectChanges();

    expect(emailInput?.errors?.['required']).toBeTruthy();
    expect(passwordInput?.errors?.['required']).toBeTruthy();
  });

  
  it('debería mostrar un mensaje de error si el inicio de sesión falla', () => {
    const invalidData = {
      correo: 'invalid@example.com',
      contrasena: 'wrongpassword',
    };

    authServiceSpy.login.and.returnValue(
      throwError({ error: { mensaje: 'Usuario o contraseña incorrectos' } })
    );
    component.loginForm.setValue(invalidData);

    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith(invalidData);
    expect(component.error).toBe('Usuario o contraseña incorrectos');
  });
});
