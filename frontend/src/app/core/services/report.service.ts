import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpParams
} from '@angular/common/http';

import { API_BASE } from '../constants/api';

import {
  PendingSection,
  SectionSummary,
  StudentAttendanceSummary,
  StudentHistoryRecord
} from '../models/report.model';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private readonly url =
    `${API_BASE}/reports`;

  constructor(
    private http: HttpClient
  ) {}

  getTodaySummary() {

    return this.http.get<
      SectionSummary[]
    >(
      `${this.url}/summary/today`
    );
  }

  getPendingSections() {

    return this.http.get<
      PendingSection[]
    >(
      `${this.url}/pending-sections`
    );
  }

getStudentHistory(
  studentId: number,
  startDate: string,
  endDate: string
) {
  const params = new HttpParams()
    .set('startDate', startDate)
    .set('endDate', endDate);

  return this.http.get<StudentHistoryRecord[]>(
    `${this.url}/students/${studentId}/history`,
    { params }
  );
}

getStudentsSummary(
  startDate: string,
  endDate: string
) {

  const params =
    new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);

  return this.http.get<
    StudentAttendanceSummary[]
  >(
    `${this.url}/students-summary`,
    { params }
  );
}
}