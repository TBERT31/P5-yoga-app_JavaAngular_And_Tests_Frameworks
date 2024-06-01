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

const mockSessions = [
  { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [1,2] },
  { id: 2, name: 'Session 2', description: 'Description 2', date: new Date(), teacher_id: 2, users: [3,4,5] }
];

describe('ListComponent for Admin', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;
  let datePipe: DatePipe;

  const mockAdminSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientTestingModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockAdminSessionService },
        { provide: SessionApiService, useValue: { all: jest.fn().mockReturnValue(of(mockSessions)) } }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    datePipe = new DatePipe('en-US'); 
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call SessionApiService on init', () => {
    expect(sessionApiService.all).toHaveBeenCalled();
  });

  it('should show Create button', () => {
    const button = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(button).not.toBeNull();
  });

  it('should show Edit button for each session', async () => {
    await fixture.whenStable();
    fixture.detectChanges();
    const buttons = fixture.debugElement.queryAll(By.css('.edit-button'));
    expect(buttons.length).toBe(mockSessions.length);
  });

  it('should display correct session information', async () => {
    await fixture.whenStable();
    fixture.detectChanges();
  
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

describe('ListComponent for User', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;

  const mockUserSessionService = {
    sessionInformation: {
      admin: false
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientTestingModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockUserSessionService },
        { provide: SessionApiService, useValue: { all: jest.fn().mockReturnValue(of(mockSessions)) } }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not show Create button', () => {
    const button = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(button).toBeNull();
  });

  it('should not show Edit button for each session', async () => {
    await fixture.whenStable();
    fixture.detectChanges();
    const buttons = fixture.debugElement.queryAll(By.css('.edit-button'));
    expect(buttons.length).toBe(0);
  });
});