import {
  Component,
  ChangeDetectorRef,
  OnInit
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ReportService } from '../../../core/services/report.service';

import {
  StudentHistoryRecord,
  StudentAttendanceSummary
} from '../../../core/models/report.model';

@Component({
  selector: 'app-student-history-page',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './student-history-page.html',
  styleUrl: './student-history-page.css'
})
export class StudentHistoryPage implements OnInit {

  studentId = 1;
  startDate = '';
  endDate = '';

  records: StudentHistoryRecord[] = [];
  studentSummaries: StudentAttendanceSummary[] = [];

  loading = false;
  errorMessage = '';

  constructor(
    private reportService: ReportService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {

    const today =
      new Date()
        .toISOString()
        .split('T')[0];

    this.startDate = today;
    this.endDate = today;

    this.loadStudentSummary();
  }

  search(): void {

    if (
      !this.studentId ||
      !this.startDate ||
      !this.endDate
    ) {

      this.errorMessage =
        'Please complete all fields';

      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.records = [];

    this.reportService
      .getStudentHistory(
        this.studentId,
        this.startDate,
        this.endDate
      )
      .subscribe({

        next: (historyData) => {

          console.log(
            'History response:',
            historyData
          );

          this.records = historyData;
          this.loading = false;

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(error);

          this.errorMessage =
            'Failed to load history';

          this.loading = false;

          this.cdr.detectChanges();
        }
      });
  }

  loadStudentSummary(): void {

    const currentYear =
      new Date().getFullYear();

    const startOfYear =
      `${currentYear}-01-01`;

    const today =
      new Date()
        .toISOString()
        .split('T')[0];

    this.reportService
      .getStudentsSummary(
        startOfYear,
        today
      )
      .subscribe({

        next: (data) => {

          console.log(
            'Summary response:',
            data
          );

          this.studentSummaries = data;

          this.cdr.detectChanges();
        },

        error: (error) => {

          console.error(error);
        }
      });
  }
}