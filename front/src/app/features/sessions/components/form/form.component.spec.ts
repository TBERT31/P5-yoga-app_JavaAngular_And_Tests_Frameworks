import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of({})),
    create: jest.fn().mockReturnValue(of({})),
    update: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of([]))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn(),
    url: ''
  };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('1')
      }
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      declarations: [FormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form on ngOnInit', () => {
    const initFormSpy = jest.spyOn(component, 'initForm');
    component.ngOnInit();
    expect(initFormSpy).toHaveBeenCalled();
  });
  
  it('should submit form', () => {
    const exitPageSpy = jest.spyOn(component, 'exitPage');
    const sessionApiServiceCreateSpy = jest.spyOn(mockSessionApiService, 'create').mockReturnValue(of({}));
    const sessionApiServiceUpdateSpy = jest.spyOn(mockSessionApiService, 'update').mockReturnValue(of({}));
  
    component.submit();
  
    if (component.onUpdate) {
      expect(sessionApiServiceUpdateSpy).toHaveBeenCalled();
    } else {
      expect(sessionApiServiceCreateSpy).toHaveBeenCalled();
    }
  
    expect(exitPageSpy).toHaveBeenCalledWith('Session created !');
  });
  
  it('should initialize form with session data when session is provided', () => {
    const session: Session = {
      name: 'Test Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Test Description',
      users: [1,2,3]
    };
  
    component.initForm(session);
  
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.get('name')?.value).toEqual(session.name);
    expect(component.sessionForm?.get('date')?.value).toEqual( new Date(session.date).toISOString().split('T')[0]); 
    expect(component.sessionForm?.get('teacher_id')?.value).toEqual(session.teacher_id);
    expect(component.sessionForm?.get('description')?.value).toEqual(session.description);
  });
  
  it('should initialize form with empty values when no session is provided', () => {
    component.initForm();
  
    expect(component.sessionForm).toBeDefined();
    expect(component.sessionForm?.get('name')?.value).toEqual('');
    expect(component.sessionForm?.get('date')?.value).toEqual('');
    expect(component.sessionForm?.get('teacher_id')?.value).toEqual('');
    expect(component.sessionForm?.get('description')?.value).toEqual('');
  });
  
  it('should navigate to sessions page on exit', () => {
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    component.exitPage('Session created !');
    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  });
});