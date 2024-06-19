import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in a user', (done) => {
    const mockSession: SessionInformation = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'username',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false,
    };

    service.logIn(mockSession);

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
      done();
    });

    expect(service.sessionInformation).toEqual(mockSession);
  });

  it('should log out a user', (done) => {
    service.logOut();

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      done();
    });

    expect(service.sessionInformation).toBeUndefined();
  });
});