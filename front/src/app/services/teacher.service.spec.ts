import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all teachers', () => {
    service.all().subscribe(teachers => {
      expect(teachers).toBeDefined();
      expect(Array.isArray(teachers)).toBeTruthy();
      expect(teachers.length).toBeGreaterThan(0);
    });
  });

  it('should get teacher detail', () => {
    const teacherId = '1'; 
    service.detail(teacherId).subscribe(teacher => {
      expect(teacher).toBeDefined();
    });
  });
  
});
