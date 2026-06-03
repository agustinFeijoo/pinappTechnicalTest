# Attendance Registration Specification

## Overview

The attendance registration module allows a teacher to record and update attendance for students belonging to a specific section on the current day.

Attendance statuses supported by the system:

* PRESENT
* ABSENT
* LATE

Business rules:

1. A student can only have one attendance record per day.
2. Attendance can only be modified for the current day.
3. A teacher can update attendance multiple times during the current day.
4. Students must belong to the selected section.
5. Attendance updates must be persisted atomically.

---

# Attendance Registration Flow

### 1. Open Section

The teacher selects a section from the available list.

### 2. Load Today's Attendance

The frontend requests the attendance data for the selected section and the current date.

The backend retrieves:

* Section information
* Students belonging to the section
* Existing attendance records for the current day (if any)

### 3. Display Student List

The frontend displays all students in the section.

For each student:

* If attendance already exists for today, the saved status is displayed.
* Otherwise, the status is empty.

### 4. Modify Attendance

The teacher selects one of the available statuses:

* PRESENT
* ABSENT
* LATE

for each student.

### 5. Submit Attendance

The teacher clicks the Save button.

The frontend sends the complete attendance payload to the backend.

### 6. Validate Request

The backend validates:

* Section exists.
* Students exist.
* Students belong to the selected section.
* Attendance is being modified for the current day.

### 7. Persist Attendance

For each student:

* If an attendance record exists for today, update it.
* Otherwise, create a new attendance record.

All changes are persisted within a single transaction.

### 8. Notify Coordinator Dashboard

After successful persistence, the system publishes an update event so coordinator dashboards receive updated attendance information in real time.

### 9. Return Success Response

The backend returns a successful response to the frontend.

---

# Save Attendance Endpoint

## Request

### Method

PUT

### URL

/api/sections/{sectionId}/attendance/today

### Request Body

```json
{
  "students": [
    {
      "studentId": 1,
      "status": "PRESENT"
    },
    {
      "studentId": 2,
      "status": "ABSENT"
    }
  ]
}
```

---

## Successful Response

### HTTP 200 OK

```json
{
  "message": "Attendance saved successfully"
}
```

---

## Error Responses

### HTTP 400 Bad Request

Returned when the request payload is invalid.

Example:

```json
{
  "message": "students must not be empty"
}
```

---

### HTTP 404 Not Found

Returned when the section or student does not exist.

Example:

```json
{
  "message": "Section not found"
}
```

---

### HTTP 409 Conflict

Returned when a business rule is violated.

Example:

```json
{
  "message": "Student does not belong to section"
}
```

---

### HTTP 500 Internal Server Error

Returned when an unexpected server error occurs.

---

# Edge Cases

## Edge Case 1: Attendance Saved Multiple Times on the Same Day

Scenario:

A teacher saves attendance and later corrects one or more student statuses on the same day.

Expected Behavior:

* Existing records are updated.
* New records are not duplicated.
* The latest status becomes the current status.

---

## Edge Case 2: Section Does Not Exist

Scenario:

The frontend sends a request for a section that is not present in the database.

Expected Behavior:

* Request is rejected.
* HTTP 404 Not Found is returned.

---

## Edge Case 3: Student Does Not Belong to the Selected Section

Scenario:

The request contains a student assigned to a different section.

Expected Behavior:

* Request is rejected.
* No attendance data is persisted.
* HTTP 409 Conflict is returned.

---

## Edge Case 4: Attempt to Modify Attendance from a Previous Day

Scenario:

A user attempts to update attendance records from a date earlier than the current day.

Expected Behavior:

* Modification is rejected.
* HTTP 409 Conflict is returned.

---

## Edge Case 5: Invalid Date Range in Student Attendance Report

Scenario:

The coordinator requests attendance history where startDate is greater than endDate.

Expected Behavior:

* Request is rejected.
* HTTP 400 Bad Request is returned.
* Validation message indicates the date range is invalid.

---

# Assumptions

1. Authentication and authorization are out of scope.
2. Teachers can only manage attendance for the current day.
3. Attendance history is immutable after the day has passed.
4. Real-time updates are implemented using Server-Sent Events (SSE).
5. Database constraints enforce uniqueness of attendance records per student per day.
