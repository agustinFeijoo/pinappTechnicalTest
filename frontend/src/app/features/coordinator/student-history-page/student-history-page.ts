import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ReportService }
from '../../../core/services/report.service';

import {
  StudentHistoryRecord
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
export class StudentHistoryPage {

  studentId = 1;

  startDate = '';

  endDate = '';

  records: StudentHistoryRecord[] = [];

  loading = false;

  errorMessage = '';

  constructor(
    private reportService: ReportService
  ) {}

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

    this.reportService
      .getStudentHistory(
        this.studentId,
        this.startDate,
        this.endDate
      )
      .subscribe({

        next: (data) => {

          this.records = data;

          this.loading = false;
        },

        error: (error) => {

          console.error(error);

          this.errorMessage =
            'Failed to load history';

          this.loading = false;
        }
      });
  }
}