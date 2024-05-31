import { expect } from '@jest/globals';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockSessionService: jest.Mocked<SessionService>;

  beforeEach(async () => {
    mockSessionApiService = {
      all: jest.fn(),
    } as unknown as jest.Mocked<SessionApiService>;

    mockSessionService = {
      sessionInformation: {
        id: 1,
        firstName: 'Admin',
        lastName: 'User',
        email: 'admin@example.com',
        token: 'token',
        type: 'admin',
        admin: true,
      },
    } as unknown as jest.Mocked<SessionService>;

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        RouterTestingModule,
        MatCardModule,
        MatIconModule,
        MatButtonModule,
      ],
      providers: [
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: SessionService, useValue: mockSessionService },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch and display sessions', () => {
    const mockSessions = [
      {
        id: 1,
        name: 'Yoga Session',
        description: 'A relaxing yoga session',
        date: new Date(),
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ];
    mockSessionApiService.all.mockReturnValueOnce(of(mockSessions));

    component.sessions$ = of(mockSessions);
    fixture.detectChanges();

    const sessionElements = fixture.debugElement.queryAll(By.css('.item'));
    expect(sessionElements.length).toBe(1);
    expect(sessionElements[0].nativeElement.textContent).toContain('Yoga Session');
  });

  it('should show create button for admin users', () => {
    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(createButton).toBeTruthy();
  });

  it('should not show create button for non-admin users', () => {
    mockSessionService.sessionInformation!.admin = false;
    fixture.detectChanges();

    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(createButton).toBeFalsy();
  });
});
