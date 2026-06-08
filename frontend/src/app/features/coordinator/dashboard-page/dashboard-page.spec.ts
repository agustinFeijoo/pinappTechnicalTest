import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { DashboardPage } from './dashboard-page';
import { ReportService } from '../../../core/services/report.service';
import { AttendanceSseService } from '../../../core/services/attendanceSSE.service';
import { provideRouter } from '@angular/router';

describe('DashboardPage', () => {

  let component: DashboardPage;
  let fixture: ComponentFixture<DashboardPage>;

const reportServiceMock = {
  getTodaySummary: vi.fn().mockReturnValue(of([])),
  getPendingSections: vi.fn().mockReturnValue(of([]))
};

const sseMock = {
  connect: vi.fn().mockReturnValue(of('updated'))
};

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [DashboardPage],
      providers: [
        provideRouter([]),
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

    fixture = TestBed.createComponent(DashboardPage);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {

    expect(component).toBeTruthy();
  });

  it('should load summary on init', () => {

    expect(
      reportServiceMock.getTodaySummary
    ).toHaveBeenCalled();
  });

  it('should load pending sections on init', () => {

    expect(
      reportServiceMock.getPendingSections
    ).toHaveBeenCalled();
  });
});