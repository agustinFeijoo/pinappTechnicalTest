export type AttendanceStatus =
  | 'PRESENT'
  | 'ABSENT'
  | 'LATE';

export interface StudentAttendance {
  studentId: number;
  fullName: string;
  status: AttendanceStatus | null;
}

export interface SectionAttendanceResponse {
  sectionId: number;
  sectionName: string;
  date: string;
  students: StudentAttendance[];
}

export interface AttendanceStudentRequest {
  studentId: number;
  status: AttendanceStatus;
}

export interface SaveAttendanceRequest {
  records: AttendanceStudentRequest[];
}