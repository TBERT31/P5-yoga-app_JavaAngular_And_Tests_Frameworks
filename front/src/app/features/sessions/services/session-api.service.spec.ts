import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { TestBed } from '@angular/core/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';
import { Observable } from 'rxjs';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() },
      { id: 2, name: 'Session 2', description: 'Description 2', date: new Date(), teacher_id: 2, users: [], createdAt: new Date(), updatedAt: new Date() }
    ];

    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should get session detail', () => {
    const sessionId = '1';
    const mockSession: Session = { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() };

    service.detail(sessionId).subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete session', () => {
    const sessionId = '1';

    service.delete(sessionId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create session', () => {
    const newSession: Session = { id: 3, name: 'New Session', description: 'New Description', date: new Date(), teacher_id: 3, users: [], createdAt: new Date(), updatedAt: new Date() };
  
    service.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });
  
    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(newSession);
  });
  
  it('should update session', () => {
    const sessionId = '1';
    const updatedSession: Session = { id: 1, name: 'Updated Session', description: 'Updated Description', date: new Date(), teacher_id: 1, users: [], createdAt: new Date(), updatedAt: new Date() };
  
    service.update(sessionId, updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });
  
    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });
  
  it('should participate in session', () => {
    const sessionId = '1';
    const userId = 'user1';
  
    service.participate(sessionId, userId).subscribe();
  
    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });
  
  it('should unparticipate in session', () => {
    const sessionId = '1';
    const userId = 'user1';
  
    service.unParticipate(sessionId, userId).subscribe();
  
    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});