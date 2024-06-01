import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';
import { MeComponent } from './me.component';
import { UserService } from '../../services/user.service';
import { SessionService } from '../../services/session.service';
import { User } from '../../interfaces/user.interface';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { SessionInformation } from '../../interfaces/sessionInformation.interface';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let router: Router;
  let matSnackBar: MatSnackBar;

  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    lastName: 'User',
    firstName: 'Test',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockAdminUser: User = {
    id: 1,
    email: 'admin@test.com',
    lastName: 'User',
    firstName: 'Test',
    admin: true,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionInformation: SessionInformation = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'test',
    firstName: 'Test',
    lastName: 'User',
    admin: false
  };

  const sessionServiceMock = {
    get sessionInformation() {
      return mockSessionInformation;
    },
    logOut: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MatSnackBarModule, RouterTestingModule, NoopAnimationsModule],
      declarations: [ MeComponent ],
      providers: [
        UserService, 
        { provide: SessionService, useValue: sessionServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get user information on init', () => {
    jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));
  
    component.ngOnInit();
    fixture.detectChanges();
  
    expect(userService.getById).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    expect(component.user).toEqual(mockUser);
  });
  
  it('should delete user account, show snackbar and log out', fakeAsync(() => {
    jest.spyOn(userService, 'delete').mockReturnValue(of(null));
    const navigateSpy = jest.spyOn(router, 'navigate');
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
  
    component.delete();
    fixture.detectChanges();
  
    expect(userService.delete).toHaveBeenCalledWith(mockSessionInformation.id.toString());
    fixture.detectChanges();
    tick(5000);  
  
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
    expect(snackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
  }));

  it('should go back when back button is clicked', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    
    const backButton = fixture.debugElement.query(By.css('[data-testid="back-button"]'));
    backButton.triggerEventHandler('click', null);
    
    expect(historySpy).toHaveBeenCalled();
  });

  it('should display basic user information correctly', () => {
    jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));
    
    component.ngOnInit();
    fixture.detectChanges();
    
    const userNameElement: HTMLElement = fixture.debugElement.nativeElement.querySelector('[data-testid="user-name"]');
    const userEmailElement: HTMLElement = fixture.debugElement.nativeElement.querySelector('[data-testid="user-email"]');
    const userAdminElement: HTMLElement = fixture.debugElement.nativeElement.querySelector('[data-testid="user-admin"]');
    const deleteButton: HTMLElement = fixture.debugElement.nativeElement.querySelector('[data-testid="delete-account"]');
    
    expect(userNameElement.textContent).toEqual(`Name: ${mockUser.firstName} ${mockUser.lastName.toUpperCase()}`);
    expect(userEmailElement.textContent).toEqual(`Email: ${mockUser.email}`);
    expect(userAdminElement).toBeNull();
    expect(deleteButton).toBeTruthy();
  });

  it('should display admin user information correctly', () => {
    jest.spyOn(userService, 'getById').mockReturnValue(of(mockAdminUser));
    
    component.ngOnInit();
    fixture.detectChanges();
    
    const userAdminElement: HTMLElement = fixture.debugElement.nativeElement.querySelector('[data-testid="user-admin"]');
    const deleteButton: HTMLElement = fixture.debugElement.nativeElement.querySelector('[data-testid="delete-account"]');
    
    expect(userAdminElement.textContent).toEqual(`You are admin`);
    expect(deleteButton).toBeNull();
  });
});