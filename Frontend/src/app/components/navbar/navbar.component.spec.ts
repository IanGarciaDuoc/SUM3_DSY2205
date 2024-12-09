// navbar.component.spec.ts
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NavbarComponent } from './navbar.component';
import { AuthService } from '../../services/auth.service';
import { BehaviorSubject, of } from 'rxjs';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let userSubject: BehaviorSubject<any>;

  const mockAdminUser = {
    id: 1,
    nombreUsuario: 'admin',
    correo: 'admin@test.com',
    rol: 'ROLE_ADMIN'
  };

  const mockRegularUser = {
    id: 2,
    nombreUsuario: 'user',
    correo: 'user@test.com',
    rol: 'ROLE_USER'
  };

  beforeEach(async () => {
    userSubject = new BehaviorSubject<any>(null);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['logout'], {
      user$: userSubject.asObservable()
    });

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        NavbarComponent
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería mostrar el enlace de inicio de sesión cuando no hay usuario', () => {
    userSubject.next(null);
    fixture.detectChanges();
    
    const loginLink = fixture.nativeElement.querySelector('a[routerLink="/login"]');
    expect(loginLink).toBeTruthy();
    expect(loginLink.textContent.trim()).toContain('Iniciar Sesión');
  });

  it('debería mostrar el nombre de usuario y botón de cerrar sesión cuando hay usuario', () => {
    userSubject.next(mockRegularUser);
    fixture.detectChanges();
    
    const username = fixture.nativeElement.querySelector('.text-gray-700');
    const logoutButton = fixture.nativeElement.querySelector('button');
    
    expect(username.textContent.trim()).toBe(mockRegularUser.nombreUsuario);
    expect(logoutButton.textContent.trim()).toBe('Cerrar Sesión');
  });

  //it('debería mostrar enlaces de administrador para usuarios admin', () => {
    //userSubject.next(mockAdminUser);
    //fixture.detectChanges();
    
    //const adminLinks = fixture.nativeElement.querySelectorAll('a[routerLink^="/admin"]');
    //expect(adminLinks.length).toBe(2);
  //});

  it('no debería mostrar enlaces de administrador para usuarios normales', () => {
    userSubject.next(mockRegularUser);
    fixture.detectChanges();
    
    const adminLinks = fixture.nativeElement.querySelectorAll('a[routerLink^="/admin"]');
    expect(adminLinks.length).toBe(0);
  });

  it('debería cambiar isMenuOpen al llamar toggleMenu', () => {
    expect(component.isMenuOpen).toBeFalse();
    
    component.toggleMenu();
    expect(component.isMenuOpen).toBeTrue();
    
    component.toggleMenu();
    expect(component.isMenuOpen).toBeFalse();
  });

  it('debería mostrar/ocultar el menú móvil según isMenuOpen', () => {
    // Menú cerrado
    expect(fixture.nativeElement.querySelector('.md\\:hidden.hidden')).toBeTruthy();
    
    // Abrir menú
    component.toggleMenu();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.md\\:hidden:not(.hidden)')).toBeTruthy();
  });

  it('debería mostrar el contador del carrito', () => {
    const cartCounter = fixture.nativeElement.querySelector('.absolute.bg-indigo-600');
    expect(cartCounter.textContent.trim()).toBe('0');
  });
});