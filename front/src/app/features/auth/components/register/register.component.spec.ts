import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { NgZone } from '@angular/core';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [AuthService],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes([
          { path: 'login', redirectTo: '' }
        ])
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form fields with empty string', () => {
    expect(component.form.get('email')?.value).toBe('');
    expect(component.form.get('firstName')?.value).toBe('');
    expect(component.form.get('lastName')?.value).toBe('');
    expect(component.form.get('password')?.value).toBe('');
  });
  
  it('should invalidate the form if email invalid', () => {
    component.form.get('email')?.setValue('not an email');
    component.form.get('firstName')?.setValue('firstName'); 
    component.form.get('lastName')?.setValue('lastName'); 
    component.form.get('password')?.setValue('password');
  
    expect(component.form.invalid).toBe(true);
  });

  it('should invalidate the form if first name invalid', () => {
    component.form.get('email')?.setValue('test@test.com');
    component.form.get('firstName')?.setValue('a'); 
    component.form.get('lastName')?.setValue('lastName'); 
    component.form.get('password')?.setValue('password');
    expect(component.form.invalid).toBe(true);

    component.form.get('firstName')?.setValue('a'.repeat(51));
    expect(component.form.invalid).toBe(true);
  });

  it('should invalidate the form if last name invalid', () => {
    component.form.get('email')?.setValue('test@test.com');
    component.form.get('firstName')?.setValue('firstName'); 
    component.form.get('lastName')?.setValue('a'); 
    component.form.get('password')?.setValue('password');
    expect(component.form.invalid).toBe(true);

    component.form.get('lastName')?.setValue('a'.repeat(51));
    expect(component.form.invalid).toBe(true);
  });

  it('should invalidate the form if password invalid', () => {
    component.form.get('email')?.setValue('test@test.com');
    component.form.get('firstName')?.setValue('firstName'); 
    component.form.get('lastName')?.setValue('lastName'); 
    component.form.get('password')?.setValue('a');
    expect(component.form.invalid).toBe(true);

    component.form.get('password')?.setValue('a'.repeat(51));
    expect(component.form.invalid).toBe(true);
  });

  it('should call AuthService.register and navigate to /login on successful registration', () => {
    const registerSpy = jest.spyOn(authService, 'register');
    const routerSpy = jest.spyOn(router, 'navigate');
    const mockRegisterRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'firstName',
      lastName: 'lastName',
      password: 'password'
    };

    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    component.form.setValue(mockRegisterRequest);
    ngZone.run(() => {
      component.submit();
    });
    expect(registerSpy).toHaveBeenCalledWith(mockRegisterRequest);
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
  });

  it('should set onError to true and display error message when AuthService.register fails', () => {
    const mockRegisterRequest: RegisterRequest = {
      email: 'test@test.com',
      firstName: 'firstName',
      lastName: 'lastName',
      password: 'password'
    };
  
    jest.spyOn(authService, 'register').mockReturnValue(throwError('error'));
    component.form.setValue(mockRegisterRequest);
    ngZone.run(() => {
      component.submit();
    });
    fixture.detectChanges();
  
    expect(component.onError).toBe(true);
  
    const errorElement = fixture.debugElement.query(By.css('.error'));
    expect(errorElement).toBeTruthy();
    expect(errorElement.nativeElement.textContent.trim()).toBe('An error occurred');
  });
});