import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';


// `describe` est une fonction de Jest qui permet de regrouper des tests liés entre eux dans une suite. Ici, nous testons le RegisterComponent.
describe('RegisterComponent', () => {
  // Déclarations de variables
  let component: RegisterComponent; // Instance du composant RegisterComponent
  let fixture: ComponentFixture<RegisterComponent>; // Wrapper pour accéder à l'instance du composant et interagir avec le template.
  let authService: AuthService; // Instance mockée du AuthService
  let router: Router; // Instance mockée du Router

  // Fonction qui s'execute avant chaque test
  beforeEach(async () => {
    // Objet mock pour AuthService avec une méthode register mockée
    const authServiceMock = {
      register: jest.fn()
    };

    // Objet mock pour Router avec une méthode navigate mockée
    const routerMock = {
      navigate: jest.fn()
    };

    // TestBed est une classe Angular utilisée pour configurer et créer un environnement de test 
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      // Fournit les dépendances du composant
      providers: [
        FormBuilder,
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent); // Crée une instance du composant et le retourne dans fixture.
    component = fixture.componentInstance; // Récupère l'instance du composant à partir de fixture
    authService = TestBed.inject(AuthService); // Injecte l'instance mockée de AuthService
    router = TestBed.inject(Router); //  Injecte l'instance mockée de Router
    fixture.detectChanges(); // Détecte les changements dans le composant et met à jour le DOM
  });

  // Test Création du Composant Angular
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test d'Initialisation du Composant
  it('should initialize form with empty fields', () => {
    const form = component.form;
    const errorMessageElement: HTMLElement | null = fixture.nativeElement.querySelector('.error');

    expect(form).toBeDefined();
    expect(form.get('email')!.value).toBe(''); // "Object is possibly 'null'" -> On utilise le '!' pour indiquer au compilateur que nous sommes sûr que le résultat ne sera pas null.
    expect(form.get('firstName')!.value).toBe('');
    expect(form.get('lastName')!.value).toBe('');
    expect(form.get('password')!.value).toBe('');

    expect(errorMessageElement).toBeNull();
  });


  // Tests de Validation du Formulaire Lorsque tous les champs sont vides (Failure)
  it('should invalidate the form and disable the submit button when fields are empty', () => {
    const form = component.form;
    const submitButton: HTMLButtonElement = fixture.nativeElement.querySelector('button[type="submit"]');
  
    form.get('email')!.setValue('');
    form.get('firstName')!.setValue('');
    form.get('lastName')!.setValue('');
    form.get('password')!.setValue('');
    
    fixture.detectChanges();
    
    // Check formulaire invalid
    expect(form.invalid).toBeTruthy();
    
    // Check btn disabled
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

  it('should validate firstName field', () => {
    const firstName = component.form.get('firstName');
    firstName!.setValue('');
    expect(firstName!.invalid).toBeTruthy();
    firstName!.setValue('Jo');
    expect(firstName!.invalid).toBeTruthy();
    firstName!.setValue('Joooooooooooooooooooo');
    expect(firstName!.invalid).toBeTruthy();
    firstName!.setValue('John');
    expect(firstName!.valid).toBeTruthy();
  });

  it('should validate lastName field', () => {
    const lastName = component.form.get('lastName');
    lastName!.setValue('');
    expect(lastName!.invalid).toBeTruthy();
    lastName!.setValue('Do');
    expect(lastName!.invalid).toBeTruthy();
    lastName!.setValue('Doooooooooooooooooooo');
    expect(lastName!.invalid).toBeTruthy();
    lastName!.setValue('Doe');
    expect(lastName!.valid).toBeTruthy();
  });

  it('should validate password field', () => {
    const password = component.form.get('password');
    password!.setValue('');
    expect(password!.invalid).toBeTruthy();
    password!.setValue('12');
    expect(password!.invalid).toBeTruthy();
    password!.setValue('120000000000000000000120000000000000000000');
    expect(password!.invalid).toBeTruthy();
    password!.setValue('123');
    expect(password!.valid).toBeTruthy();
  });
  
  // Test de Validation du Formulaire (Success)
  it('should validate the form when all fields are filled', () => {
    const form = component.form;
    form.get('email')!.setValue('test@example.com');
    form.get('firstName')!.setValue('John');
    form.get('lastName')!.setValue('Doe');
    form.get('password')!.setValue('password123');
    expect(form.valid).toBeTruthy();
  });

  // Test de Soumissions du Formulaire (Success)
  it('should call authService.register on form submit', () => {
    const form = component.form;

    // Remplit le formulaire avec des valeurs de test
    form.get('email')!.setValue('test@example.com');
    form.get('firstName')!.setValue('John');
    form.get('lastName')!.setValue('Doe');
    form.get('password')!.setValue('password123');
  
    // Espionne la méthode register et remplace son retour par un Observable<void>
    const registerSpy = jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
  
    // Appelle la méthode submit du RegisterComponent
    component.submit();
  
    // Vérifie que register a été appelé avec les bonnes valeurs.
    expect(registerSpy).toHaveBeenCalledWith({
      email: 'test@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
  });
  
  // Test Naviguation Après Inscription Réussie
  it('should navigate to /login on successful registration', () => {
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
  
    const form = component.form;
    form.get('email')!.setValue('test@example.com');
    form.get('firstName')!.setValue('John');
    form.get('lastName')!.setValue('Doe');
    form.get('password')!.setValue('password123');
  
    component.submit();
  
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
  
  it('should set onError to true on registration error and show error message', () => {
    // Simuler une erreur lors de l'appel à authService.register
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('An error occurred')));
  
    const form = component.form;
    form.get('email')!.setValue('example@test.com');
    form.get('firstName')!.setValue('John');
    form.get('lastName')!.setValue('Doe');
    form.get('password')!.setValue('password123');
  
    // Appeler la méthode submit qui déclenchera l'erreur
    component.submit();
  
    // Vérifier que la propriété onError est définie à true
    expect(component.onError).toBe(true);
  
    // Vérifier que le message d'erreur est affiché dans le template
    fixture.detectChanges(); // Mettre à jour la vue pour refléter les changements
    const errorMessageElement: HTMLElement | null = fixture.nativeElement.querySelector('.error');
    expect(errorMessageElement).not.toBeNull(); // Vérifier que l'élément contenant le message d'erreur est rendu
  });
  
});
