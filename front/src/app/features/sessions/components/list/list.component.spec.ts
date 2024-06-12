import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { ListComponent } from './list.component';
import { DatePipe } from '@angular/common';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ReactiveFormsModule } from '@angular/forms';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { NO_ERRORS_SCHEMA } from '@angular/core';



const mockSessions = [
  { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [1,2] },
  { id: 2, name: 'Session 2', description: 'Description 2', date: new Date(), teacher_id: 2, users: [3,4,5] }
];

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientTestingModule, 
        MatCardModule, 
        MatIconModule, 
        MatSnackBarModule, 
        ReactiveFormsModule
      ],
      providers: [
        { 
          provide: SessionApiService, useValue: { all: jest.fn().mockReturnValue(of(mockSessions)) }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    sessionService = TestBed.inject(SessionService);

    Object.defineProperty(sessionService, 'sessionInformation', {
      get: jest.fn().mockReturnValue({
        token: 'some-token',
        type: 'user',
        id: 2,
        username: 'regularUser',
        firstName: 'Regular',
        lastName: 'User',
        admin: false
      })
    });

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call SessionApiService on init', () => {
    expect(sessionApiService.all).toHaveBeenCalled();
  });


  it('should display the Create button when user is admin',  () => {
    jest.spyOn(sessionService, 'sessionInformation', 'get').mockReturnValue({
      token: 'some-token',
      type: 'admin',
      id: 1,
      username: 'adminUser',
      firstName: 'Admin',
      lastName: 'User',
      admin: true
    });

    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(button).not.toBeNull();
  });

  it('should not display the Create button when user is not admin', () => {
    const button = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(button).toBeNull();
  });

  it('should display Edit buttons for each session when user is admin', () => {
    jest.spyOn(sessionService, 'sessionInformation', 'get').mockReturnValue({
      token: 'some-token',
      type: 'admin',
      id: 1,
      username: 'adminUser',
      firstName: 'Admin',
      lastName: 'User',
      admin: true
    });

    fixture.detectChanges();
    const buttons = fixture.debugElement.queryAll(By.css('.edit-button'));
    expect(buttons.length).toBe(mockSessions.length);
  });

  it('should not display Edit buttons for any session when user is not admin', () => {
    const buttons = fixture.debugElement.queryAll(By.css('.edit-button'));
    expect(buttons.length).toBe(0);
  });


  it('should display correct session information', () => {
    const datePipe = new DatePipe('en-US');
    const cardTitles = fixture.debugElement.queryAll(By.css('.session-name'));
    const cardSubtitles = fixture.debugElement.queryAll(By.css('.session-date'));
    const cardContents = fixture.debugElement.queryAll(By.css('.session-description'));
  
    for (let i = 0; i < mockSessions.length; i++) {
      expect(cardTitles[i].nativeElement.textContent).toContain(mockSessions[i].name);
      expect(cardSubtitles[i].nativeElement.textContent).toContain(datePipe.transform(mockSessions[i].date, 'longDate'));
      expect(cardContents[i].nativeElement.textContent).toContain(mockSessions[i].description);
    }
  });
  
});