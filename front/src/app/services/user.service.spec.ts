import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    const userId = '1'; 
    service.getById(userId).subscribe(user => {
      expect(user).toBeDefined();
    });
  });

  it('should delete user by id', () => {
    const userId = '1';
    const expectedResponse = {}; 

    // Envoyer la requête de suppression de l'utilisateur
    service.delete(userId).subscribe(response => {
      expect(response).toEqual(expectedResponse);
    });

  });
});
