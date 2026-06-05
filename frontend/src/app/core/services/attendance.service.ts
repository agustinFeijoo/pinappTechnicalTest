import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { API_BASE } from '../constants/api';
import {
  SaveAttendanceRequest,
  SectionAttendanceResponse
} from '../models/attendance.model';

@Injectable({ providedIn: 'root' })
export class AttendanceService {

  private readonly url = `${API_BASE}/sections`;

  constructor(private http: HttpClient) {}

  getTodayAttendance(sectionId: number) {
    return this.http.get<SectionAttendanceResponse>(
      `${this.url}/${sectionId}/attendance/today`
    );
  }

  saveAttendance(sectionId: number, request: SaveAttendanceRequest) {
    return this.http.put(
      `${this.url}/${sectionId}/attendance/today`,
      request
    );
  }
}
