import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { DashboardPage } from './dashboard-page';
import { ReportService } from '../../../core/services/report.service';
import { SseService } from '../../../core/services/sse.service';

describe('DashboardPage', () => {
  let fixture: ComponentFixture<DashboardPage>;

  const mockSummaries = [
    {
      sectionId: 1,
      sectionName: '3A',
      gradeName: '3rd Grade',
      presentCount: 4,
      absentCount: 1,
      lateCount: 1
    },
    {
      sectionId: 2,
      sectionName: '3B',
      gradeName: '3rd Grade',
      presentCount: 2,
      absentCount: 2,
      lateCount: 2
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardPage],
      providers: [
        provideRouter([]),
        {
          provide: ReportService,
          useValue: {
            getTodaySummary: () => of(mockSummaries),
            getPendingSections: () =>
              of([{ sectionId: 2, sectionName: '3B', gradeName: '3rd Grade' }])
          }
        },
        {
          provide: SseService,
          useValue: {
            connectAttendanceUpdates: () => of(null)
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardPage);
    fixture.detectChanges();
    await fixture.whenStable();
    fixture.detectChanges();
  });

  it('shows coordinator summary totals per section', () => {
    const text = fixture.nativeElement.textContent as string;
    expect(text).toContain('3A');
    expect(text).toContain('4');
    expect(text).toContain('3B');
    expect(text).toContain('2');
  });

  it('lists pending sections', () => {
    const text = fixture.nativeElement.textContent as string;
    expect(text).toContain('Secciones pendientes');
    expect(text).toContain('3B');
  });
});
