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
  date: string;
  status: AttendanceStatus;
}

export interface StudentHistoryResponse {
  studentId: number;
  studentName: string;
  startDate: string;
  endDate: string;
  records: StudentHistoryRecord[];
}
