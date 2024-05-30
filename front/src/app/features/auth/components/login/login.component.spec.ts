import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder,ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  // On déclare les variables 
  let component: LoginComponent; // Instance du composant LoginComponent
  let fixture: ComponentFixture<LoginComponent>; // Wrapper pour accéder à l'instance du composant et interagir avec le template.
  let authService: AuthService; // Instance mockée du AuthService
  let router: Router; // Instance mockée du Router
  let sessionService: SessionService; // Instance mockée du SessionService

  beforeEach(async () => {
    // On initialise chacun des objets mock pour chaque service et leur fonction associée
    const authServiceMock = {
      login: jest.fn()
    };

    const routerMock = {
      navigate: jest.fn()
    };

    const sessionServiceMock = {
      logIn: jest.fn()
    };

    // TestBed est une classe Angular utilisée pour configurer et créer un environnement de test 
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent); // Crée une instance du composant et le retourne dans fixture.
    component = fixture.componentInstance;  // Récupère l'instance du composant à partir de fixture
    authService = TestBed.inject(AuthService); // Injecte l'instance mockée de AuthService
    router = TestBed.inject(Router); // Injecte l'instance mockée de Router
    sessionService = TestBed.inject(SessionService); // Injecte l'instance mockée de SessionService
    fixture.detectChanges(); // Détecte les changements dans le composant et met à jour le DOM
  });

  // Test Création du Composant Angular
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test d'Initialisation du Composant
  it('should initialize form with empty fields and no error', () => {
    const form = component.form;
    const errorMessageElement: HTMLElement | null = fixture.nativeElement.querySelector('.error');

    expect(form).toBeDefined();
    expect(form.get('email')!.value).toBe('');
    expect(form.get('password')!.value).toBe('');

    expect(errorMessageElement).toBeNull();
  });

  // Tests de Validation du Formulaire Lorsque tous les champs sont vides (Failure)
  it('should invalidate the form and disable the submit button when fields are empty', () => {
    const form = component.form;
    const submitButton: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');

    form.get('email')!.setValue('');
    form.get('password')!.setValue('');

    fixture.detectChanges();

    expect(form.invalid).toBeTruthy();
    expect(submitButton.disabled).toBeTruthy();
  });

  // Tests des Validators pour chaque champs du formulaire 
  it('should validate email field', () => {
    const email = component.form.get('email');
    email!.setValue('');
    expect(email!.invalid).toBeTruthy();
    email!.setValue('invalid-email');
    expect(email!.invalid).toBeTruthy();
    email!.setValue('example@test.com');
    expect(email!.valid).toBeTruthy();
  });

  it('should validate password field', () => {
    const password = component.form.get('password');
    password!.setValue('');
    expect(password!.invalid).toBeTruthy();
    password!.setValue('12');
    expect(password!.invalid).toBeTruthy();
    password!.setValue('123');
    expect(password!.valid).toBeTruthy();
  });

  // Test de Validation du Formulaire (Success)
  it('should validate the form when all fields are filled', () => {
    const form = component.form;
    form.get('email')!.setValue('example@test.com');
    form.get('password')!.setValue('password123');
    expect(form.valid).toBeTruthy();
  });

  // Test de Soumissions du Formulaire (Success)
  it('should call authService.login on form submit', () => {
    const form = component.form;
    
    // Mock simulant une interface SessionInformation 
    const mockSessionInfo: SessionInformation = {
      token: 'testToken',
      type: 'Bearer',
      id: 1,
      username: 'example@test.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    // Remplit le formulaire avec des valeurs de test
    form.get('email')!.setValue('example@test.com');
    form.get('password')!.setValue('password123');

    // Espionne la méthode login et remplace son retour par un Observable<SessionInformation>
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInfo));

    // Appelle la méthode submit du LoginComponent
    component.submit();

     // Vérifie que login a été appelé avec les bonnes valeurs.
    expect(loginSpy).toHaveBeenCalledWith({
      email: 'example@test.com',
      password: 'password123'
    });
  });

  it('should navigate to /sessions on successful login', () => {
    // Mock simulant une interface SessionInformation 
    const mockSessionInfo: SessionInformation = {
      token: 'testToken',
      type: 'Bearer',
      id: 1,
      username: 'example@test.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };


    // Espionne la méthode login et remplace son retour par un Observable<SessionInformation>
    jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInfo));
    // Espionne la méthode logIn de sessionService et remplace son implémentation par une fonction vide
    jest.spyOn(sessionService, 'logIn').mockImplementation(() => {});

    const form = component.form;
    
    // Remplit le formulaire avec des valeurs de test
    form.get('email')!.setValue('example@test.com');
    form.get('password')!.setValue('password123');

    // Appelle la méthode submit du LoginComponent
    component.submit();

    // Vérifie que la méthode logIn de sessionService a été appelée avec l'objet mockSessionInfo
    expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInfo);
    // Vérifie que le routeur a navigué vers la route '/sessions' après la soumission du formulaire
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true on login error and show error message', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('An error occurred')));

    const form = component.form;
    form.get('email')!.setValue('example@test.com');
    form.get('password')!.setValue('password123');

    component.submit();

    expect(component.onError).toBe(true);

    fixture.detectChanges();
    const errorMessageElement: HTMLElement | null = fixture.nativeElement.querySelector('.error');
    expect(errorMessageElement).not.toBeNull();
  });


});
