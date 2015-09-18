# Grouping/attendance API

[Back to APIv2](./APIv2.md#api-v2)

[Grouping API](#grouping-api)

[Attendance API](#attendance-api)

# Grouping API

## Relevant classes

[ValidGroupingDTO](../../src/main/java/com/superum/api/v2/grouping/ValidGroupingDTO.java)

### Commands

#### Create grouping
```
    PUT   /grouping
    BODY  ValidGroupingDTO
    RET   void
```

Creates a grouping of students into a group

It will fail if:
  * HTTP 404; if a record for given group/student ids doesn't exist;
  * HTTP 409; if this group already has a grouping;

Returns HTTP 200 OK if it succeeds

------

#### Update grouping
```
    POST  /grouping
    BODY  ValidGroupingDTO
    RET   void
```

Updates a grouping of students into a group

It will fail if:
  * HTTP 404; if a record for given group/student ids doesn't exist;
  * HTTP 404; if this group doesn't have a grouping yet;

Returns HTTP 200 OK if it succeeds

------

#### Delete grouping
```
    DELETE   /grouping/{field}/{id}
             field      String      group or student
             id         int         1 <= id <= MAX_INT
    RET      void
```

Deletes all records of grouping for given field

It will fail if:
  * HTTP 404; if a record for given group/student ids doesn't exist;
  * HTTP 404; if this group/student doesn't have a grouping yet;

Returns HTTP 200 OK if it succeeds

### Queries

In order to make queries, use [/group/student/{id}](./Group.md#read-for-table) or [/student/group/{id}](./Student.md#read-for-table)

# Attendance API

## Relevant classes

[ValidLessonAttendanceDTO](../../src/main/java/com/superum/api/v2/attendance/ValidLessonAttendanceDTO.java)

### Commands

#### Create attendance
```
    PUT   /lesson/attendance
    BODY  ValidLessonAttendanceDTO
    RET   void
```

Creates attendance record for a lesson

It will fail if:
  * HTTP 400; if any of the students do not belong to the group this lesson was for;
  * HTTP 404; if a record for given lesson/student ids doesn't exist;
  * HTTP 409; if this lesson already has an attendance record

Returns HTTP 200 OK if it succeeds

------

#### Update attendance
```
    POST  /lesson/attendance
    BODY  ValidLessonAttendanceDTO
    RET   void
```

Updates attendance record for a lesson

It will fail if:
  * HTTP 400; if any of the students do not belong to the group this lesson was for;
  * HTTP 404; if a record for given lesson/student ids doesn't exist;
  * HTTP 404; if this lesson doesn't have an attendance record yet;

Returns HTTP 200 OK if it succeeds

------

#### Delete attendance
```
    DELETE   /lesson/attendance/{field}/{id}
             field      String      lesson or student
             id         long        1 <= id <= MAX_LONG
    RET      void
```

Deletes all records of attendance for given field

It will fail if:
  * HTTP 404; if a record for given lesson/student ids doesn't exist;
  * HTTP 404; if this lesson/student doesn't have an attendance record yet;

Returns HTTP 200 OK if it succeeds

### Queries

In order to make queries, use [/lesson/student/{id}](./Lesson.md#read-for-table) or [/student/lesson/{id}](./Student.md#read-for-table)
