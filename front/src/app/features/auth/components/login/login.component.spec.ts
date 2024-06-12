import { HttpClientModule } from '@angular/common/http';
import { expect } from '@jest/globals';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { NgZone } from '@angular/core';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { LoginComponent } from './login.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { MatSnackBarModule } from '@angular/material/snack-bar';

fdescribe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, AuthService],
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'sessions', redirectTo: '' } 
        ]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty fields', () => {
    expect(component.form.get('email')!.value).toBe('');
    expect(component.form.get('password')!.value).toBe('');
  });

  it('should call AuthService.login with form values when form is valid', () => {
    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(of({} as SessionInformation));
    const formValues = { email: 'test@test.com', password: 'password' };
    component.form.setValue(formValues);
    ngZone.run(() => { 
      component.submit();
    });
    fixture.detectChanges();
    expect(loginSpy).toHaveBeenCalledWith(formValues);
  });

  it('should not call AuthService.login when form is invalid', () => {
    const loginSpy = jest.spyOn(authService, 'login');
    component.form.setValue({ email: '', password: '' });
    expect(component.form.invalid).toBeTruthy();
    expect(loginSpy).not.toHaveBeenCalled();
  });

  it('should not call AuthService.login when email is not valid', () => {
    const loginSpy = jest.spyOn(authService, 'login');
    component.form.setValue({ email: 'not a valid email', password: 'password' });
    expect(component.form.invalid).toBeTruthy();
    expect(loginSpy).not.toHaveBeenCalled();
  });
  
  it('should not call AuthService.login when password is less than 3 characters', () => {
    const loginSpy = jest.spyOn(authService, 'login');
    component.form.setValue({ email: 'test@test.com', password: 'ab' });
    expect(component.form.invalid).toBeTruthy();
    expect(loginSpy).not.toHaveBeenCalled();
  });

  it('should handle error from AuthService.login and display error message', () => {
    jest.spyOn(authService, 'login').mockReturnValue(throwError('error'));
    component.form.setValue({ email: 'test@test.com', password: 'password' });
    component.submit();
    fixture.detectChanges();
    expect(component.onError).toBe(true);
    const errorMessage = fixture.debugElement.nativeElement.querySelector('.error');
    expect(errorMessage).toBeTruthy();
    expect(errorMessage.textContent).toContain('An error occurred');
  });

  it('should call SessionService.logIn and navigate to /sessions on successful login', () => {
    const sessionSpy = jest.spyOn(sessionService, 'logIn');
    const routerSpy = jest.spyOn(router, 'navigate');
    const mockSession: SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'username',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false
    };

    jest.spyOn(authService, 'login').mockReturnValue(of(mockSession));
    component.form.setValue({ email: 'test@test.com', password: 'password' });
     ngZone.run(() => { 
      component.submit();
    });
    fixture.detectChanges();
    expect(sessionSpy).toHaveBeenCalledWith(mockSession);
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });
});