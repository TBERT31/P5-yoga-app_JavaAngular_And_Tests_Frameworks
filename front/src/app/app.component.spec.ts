import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { AuthService } from './features/auth/services/auth.service';
import { SessionService } from './services/session.service';
import { SessionInformation } from './interfaces/sessionInformation.interface';

describe('AppComponent', () => {
  let mockAuthService: Partial<AuthService>;
  let mockSessionService: Partial<SessionService>;
  let mockRouter: Partial<Router>;

  beforeEach(async () => {
    mockAuthService = {
      register: jest.fn().mockReturnValue(of(undefined)), // Mocking register method
      login: jest.fn().mockReturnValue(of({} as SessionInformation)), // Mocking login method
    };

    mockSessionService = {
      logOut: jest.fn(),
      $isLogged: jest.fn().mockReturnValue(of(true)),
    };

    mockRouter = {
      navigate: jest.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule,
      ],
      declarations: [
        AppComponent,
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter as Router }, // Cast to correct type
        HttpClient, // Include HttpClient here to resolve dependency injection
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should call $isLogged from SessionService', (done) => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.$isLogged().subscribe((loggedIn) => {
      expect(loggedIn).toBe(true);
      expect(mockSessionService.$isLogged).toHaveBeenCalled();
      done();
    });
  });

  it('should call logOut from SessionService and navigate to root', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    app.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });


});
