import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in', () => {
    const user = {
      token: 'someToken',
      type: 'user',
      id: 1,
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };
    service.logIn(user);
    expect(service.sessionInformation).toEqual(user);
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBeTruthy();
    });
  });

  it('should log out', () => {
    const user = {
      token: 'someToken',
      type: 'user',
      id: 1,
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };
    service.logIn(user);
    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBeFalsy();
    });
  });
  
});
