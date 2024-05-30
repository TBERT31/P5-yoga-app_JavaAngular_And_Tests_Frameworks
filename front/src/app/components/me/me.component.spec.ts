import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { User } from '../../interfaces/user.interface';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: jest.Mocked<UserService>;
  let sessionService: jest.Mocked<SessionService>;
  let router: jest.Mocked<Router>;
  let matSnackBar: jest.Mocked<MatSnackBar>;

  const mockUser: User = {
    id: 1,
    email: 'example@test.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: false,
    password: 'password123',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    const userServiceMock = {
      getById: jest.fn(),
      delete: jest.fn(),
    };

    const routerMock = {
      navigate: jest.fn(),
    };

    const matSnackBarMock = {
      open: jest.fn(),
    };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: routerMock },
        { provide: MatSnackBar, useValue: matSnackBarMock }
      ],
    }).compileComponents();

      fixture = TestBed.createComponent(MeComponent);
      component = fixture.componentInstance;
      userService = TestBed.inject(UserService) as jest.Mocked<UserService>;
      sessionService = TestBed.inject(SessionService) as jest.Mocked<SessionService>;
      router = TestBed.inject(Router) as jest.Mocked<Router>;
      matSnackBar = TestBed.inject(MatSnackBar) as jest.Mocked<MatSnackBar>;
  
      userService.getById.mockReturnValue(of(mockUser)); //  On fait en sorte que la méthode getById de mockReturnValue renvoie l'objet mockUser
      fixture.detectChanges(); // ngOnInit est appelé ici
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user information on init', () => {
    expect(userService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should navigate back when back button is clicked', () => {
    jest.spyOn(window.history, 'back');
    component.back();
    expect(window.history.back).toHaveBeenCalled();
  });

  it('should delete user account and navigate to home on delete', () => {
    userService.delete.mockReturnValue(of({}));

    // Mock the logOut function in sessionService
    sessionService.logOut = jest.fn();

    component.delete();

    expect(userService.delete).toHaveBeenCalledWith('1');
    expect(matSnackBar.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(sessionService.logOut).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });
});
