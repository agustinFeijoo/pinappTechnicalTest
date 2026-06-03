# Asistec - School Attendance Management System

## Overview

Asistec is a full-stack application that allows teachers to register daily attendance and academic coordinators to monitor attendance metrics in real time.

### Roles

* **Teacher**

  * View students assigned to a section.
  * Register attendance for the current day.
  * Update attendance records for the current day.

* **Coordinator**

  * View attendance summaries by section.
  * Review student attendance history.
  * Identify sections that have not registered attendance.
  * Receive real-time updates without refreshing the page.

---

# Technology Stack

## Backend

* Java 21
* Spring Boot 3
* Spring Data JPA
* H2 Database
* Maven

## Frontend

* Angular 21
* TypeScript
* RxJS

---

# Installation and Execution

## Prerequisites

* Java 21+
* Maven 3.9+
* Node.js 22+
* Angular CLI

## Run Backend

```bash
mvn spring-boot:run
```

Backend will be available at:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

---

## Run Frontend

```bash
ng serve
```

Frontend will be available at:

```text
http://localhost:4200
```

---

## Run Tests

Backend:

```bash
mvn test
```

Frontend:

```bash
ng test --watch=false
```

---

# Initial Seed Data

The application loads sample data automatically at startup:

* 2 Grades
* 4 Sections (3A, 3B, 4A, 4B)
* Multiple students per section
* Attendance records for the last 5 business days
* At least one section without attendance for the current day

---

# Screenshots

## Teacher Module

[Insert screenshot here]

Features:

* View students by section
* Register attendance
* Update attendance for current day
* Prevent duplicate records

---

## Coordinator Module

[Insert screenshot here]

Features:

* Attendance summary by section
* Student attendance history
* Pending sections report
* Real-time updates

---

# Design Decisions

## How did you model the relationship Section → Student → Attendance Record?

The domain model follows the natural hierarchy of the school structure.

```text
Grade
 └── Section
      └── Student
            └── AttendanceRecord
```

Relationships:

* A Grade contains multiple Sections.
* A Section contains multiple Students.
* A Student can have multiple Attendance Records.
* Each Attendance Record belongs to exactly one Student and one attendance date.

This approach keeps attendance history independent from student information and allows efficient reporting by section, grade, and student.

---

## How did you guarantee that a student cannot have two attendance records on the same day?

The solution combines business validation and database constraints.

At the database level, a unique constraint is defined on:

```text
(student_id, attendance_date)
```

This guarantees that duplicate records cannot be persisted even in concurrent scenarios.

At the service layer, existing attendance records for the current day are updated instead of creating new records.

This dual-layer approach protects both business logic and data integrity.

---

## What strategy did you use to keep coordinator data updated without refreshing?

The coordinator dashboard receives updates through Server-Sent Events (SSE).

When attendance is saved:

1. The backend persists attendance records.
2. An event is published.
3. Connected coordinator dashboards receive the update.
4. The frontend refreshes the affected views automatically.

This provides near real-time updates while keeping implementation complexity low compared to WebSockets.

---

## What did you intentionally leave out? What would you do differently with more time?

The following items were intentionally excluded to keep the solution focused on the requested requirements:

* Authentication and authorization
* Audit logging
* Pagination
* Advanced filtering
* Export to Excel/PDF
* Production-grade monitoring and observability

With more time I would:

* Implement JWT authentication.
* Add optimistic locking to handle concurrent updates.
* Introduce a CQRS-based reporting layer.
* Add OpenAPI/Swagger documentation.
* Add Docker and Docker Compose support.
* Add end-to-end tests with Cypress.
* Deploy the application using a CI/CD pipeline.

---

# Assumptions

* Teachers can only modify attendance for the current day.
* Historical attendance records are read-only.
* A student belongs to exactly one section.
* Attendance status can only be PRESENT, ABSENT, or LATE.

---

# Author

Agustín Feijóo
