import { AttendanceStatus } from './attendance.model';

export interface SectionSummary {
  sectionId: number;
  sectionName: string;
  gradeName?: string;
  presentCount: number;
  absentCount: number;
  lateCount: number;
}

export interface PendingSection {
  sectionId: number;
  sectionName: string;
  gradeName?: string;
}

export interface StudentHistoryRecord {
  studentId: number;
  studentName: string;
  attendanceDate: string;
  status: AttendanceStatus;
}
export interface StudentHistoryResponse {
  studentId: number;
  studentName: string;
  startDate: string;
  endDate: string;
  records: StudentHistoryRecord[];
}

export interface StudentAttendanceSummary {
  studentId: number;
  firstName: string;
  lastName: string;
  presentCount: number;
  lateCount: number;
  absentCount: number;
}
