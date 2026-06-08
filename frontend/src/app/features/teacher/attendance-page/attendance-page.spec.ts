import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { AttendancePage } from './attendance-page';

import { AttendanceService }
from '../../../core/services/attendance.service';

import { SectionService }
from '../../../core/services/section.service';

describe('AttendancePage', () => {

  let component: AttendancePage;
  let fixture: ComponentFixture<AttendancePage>;

  const attendanceServiceMock = {

  getTodayAttendance: vi.fn()
    .mockReturnValue(
      of({
        sectionId: 1,
        sectionName: '3A',
        date: '2026-06-08',
        students: []
      })
    ),

  saveAttendance: vi.fn()
    .mockReturnValue(of({}))
};
 const sectionServiceMock = {

  getSections: vi.fn()
    .mockReturnValue(
      of([
        {
          id: 1,
          name: '3A'
        }
      ])
    )
};

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [AttendancePage],
      providers: [
        provideRouter([]),
        {
          provide: AttendanceService,
          useValue: attendanceServiceMock
        },
        {
          provide: SectionService,
          useValue: sectionServiceMock
        }
      ]
    }).compileComponents();

    fixture =
      TestBed.createComponent(AttendancePage);

    component =
      fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {

    expect(component).toBeTruthy();
  });

  it('should load sections', () => {

    expect(
      sectionServiceMock.getSections
    ).toHaveBeenCalled();
  });

  it('should load attendance', () => {

    expect(
      attendanceServiceMock.getTodayAttendance
    ).toHaveBeenCalled();
  });
});