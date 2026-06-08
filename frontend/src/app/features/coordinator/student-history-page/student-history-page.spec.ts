import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { StudentHistoryPage } from './student-history-page';
import { ReportService } from '../../../core/services/report.service';
import { AttendanceSseService } from '../../../core/services/attendanceSSE.service';

describe('StudentHistoryPage', () => {

  let component: StudentHistoryPage;
  let fixture: ComponentFixture<StudentHistoryPage>;

  const reportServiceMock = {

  getStudentHistory: vi.fn()
    .mockReturnValue(of([])),

  getStudentsSummary: vi.fn()
    .mockReturnValue(of([]))
};

const sseMock = {
  connect: vi.fn()
    .mockReturnValue(of('updated'))
};

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [StudentHistoryPage],
      providers: [
        {
          provide: ReportService,
          useValue: reportServiceMock
        },
        {
          provide: AttendanceSseService,
          useValue: sseMock
        }
      ]
    }).compileComponents();

    fixture =
      TestBed.createComponent(StudentHistoryPage);

    component =
      fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {

    expect(component).toBeTruthy();
  });

  it('should load student summary', () => {

    expect(
      reportServiceMock.getStudentsSummary
    ).toHaveBeenCalled();
  });

  it('should search history', () => {

    component.studentId = 1;
    component.startDate = '2026-01-01';
    component.endDate = '2026-06-01';

    component.search();

    expect(
      reportServiceMock.getStudentHistory
    ).toHaveBeenCalled();
  });
});