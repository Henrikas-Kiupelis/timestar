Grouping API:

1)
    PUT   /grouping
    BODY  com.superum.api.grouping.ValidGroupingDTO
    RET   void

    Creates a grouping of students into a group;

    It will fail if:
    a) HTTP 404; if a record for given group/student ids doesn't exist;
    b) HTTP 409; if this group already has a grouping;

    Returns HTTP 200 OK if it succeeds

2)
    POST  /grouping
    BODY  com.superum.api.grouping.ValidGroupingDTO
    RET   void

    Updates a grouping of students into a group;

    It will fail if:
    a) HTTP 404; if a record for given group/student ids doesn't exist;
	b) HTTP 404; if this group doesn't have a grouping yet;

    Returns HTTP 200 OK if it succeeds

3)
    DELETE   /grouping/{field}/{id}
             field      String      group or student
             id         int         1 <= id <= MAX_INT
    RET      void

    Deletes all records of grouping for given field;

    It will fail if:
    a) HTTP 404; if a record for given group/student ids doesn't exist;
	b) HTTP 404; if this group/student doesn't have a grouping yet;

    Returns HTTP 200 OK if it succeeds


Attendance API:

1)
    PUT   /lesson/attendance
    BODY  com.superum.api.attendance.ValidLessonAttendanceDTO
    RET   void

    Creates attendance record for a lesson;

    It will fail if:
    a) HTTP 400; if any of the students do not belong to the group this lesson was for;
    b) HTTP 404; if a record for given lesson/student ids doesn't exist;
    c) HTTP 409; if this lesson already has an attendance record

    Returns HTTP 200 OK if it succeeds

2)
    POST  /lesson/attendance
    BODY  com.superum.api.attendance.ValidLessonAttendanceDTO
    RET   void

    Updates attendance record for a lesson;

    It will fail if:
    a) HTTP 400; if any of the students do not belong to the group this lesson was for;
    b) HTTP 404; if a record for given lesson/student ids doesn't exist;
    c) HTTP 404; if this lesson doesn't have an attendance record yet;

    Returns HTTP 200 OK if it succeeds

3)
    DELETE   /lesson/attendance/{field}/{id}
             field      String      lesson or student
             id         long        1 <= id <= MAX_LONG
    RET      void

    Deletes all records of attendance for given field;

    It will fail if:
    a) HTTP 404; if a record for given lesson/student ids doesn't exist;
    b) HTTP 404; if this lesson/student doesn't have an attendance record yet;

    Returns HTTP 200 OK if it succeeds
