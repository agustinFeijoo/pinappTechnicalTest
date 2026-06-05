import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import {
  SectionSummary,
  StudentHistoryRecord,
  PendingSection
} from '../models/report.model';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private readonly api =
    'http://localhost:8080/api/reports';

  constructor(
    private http: HttpClient
  ) {}

  getTodaySummary() {
    return this.http.get<SectionSummary[]>(
      `${this.api}/summary/today`
    );
  }

  getStudentHistory(
    studentId: number,
    startDate: string,
    endDate: string
  ) {
    return this.http.get<StudentHistoryRecord[]>(
      `${this.api}/students/${studentId}` +
      `?startDate=${startDate}` +
      `&endDate=${endDate}`
    );
  }

  getPendingSections() {
    return this.http.get<PendingSection[]>(
      `${this.api}/pending-sections`
    );
  }
}