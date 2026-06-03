import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import {
  SaveAttendanceRequest,
  SectionAttendanceResponse
} from '../models/attendance.model';

@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private readonly api =
    'http://localhost:8080/api/sections';

  constructor(
    private http: HttpClient
  ) {}

  getTodayAttendance(sectionId: number) {

    return this.http.get<SectionAttendanceResponse>(
      `${this.api}/${sectionId}/attendance/today`
    );
  }

  saveAttendance(
    sectionId: number,
    request: SaveAttendanceRequest
  ) {

    return this.http.put(
      `${this.api}/${sectionId}/attendance/today`,
      request
    );
  }
}