Lesson API:

1)
    PUT   /lesson
    BODY  com.superum.api.lesson.ValidLessonDTO
    RET   com.superum.api.lesson.ValidLessonDTO

    Creates a new lesson

    It will fail if:
    a) HTTP 400; the id field was set;
    b) HTTP 400; a mandatory field was not set;
    c) HTTP 409; the lesson overlaps with another lesson for the teacher of this group;

    Returned lesson will have its id field set

2)
    POST  /lesson
    BODY  com.superum.api.lesson.ValidLessonDTO
    RET   void

    Updates a lesson

    It will fail if:
    a) HTTP 400; the id field was not set;
    b) HTTP 400; only the id field was set and no other fields were;
    c) HTTP 404; no lesson with provided id exists;
    d) HTTP 409; the lesson overlaps with another lesson for the teacher of this group;

    Returns HTTP 200 OK if it succeeds

3)
    DELETE  /lesson/{lessonId}
            lessonId        long           1 <= lessonId <= MAX_LONG
    RET     void

    Deletes a lesson

    It will fail if:
    a) HTTP 400; lesson cannot be deleted because it is still used;
    a) HTTP 404; no lesson with provided id exists;

    Returns HTTP 200 OK if it succeeds

4)
    GET  /lesson/{lessonId}
         lessonId        long           1 <= lessonId <= MAX_LONG
    RET  com.superum.api.lesson.ValidLessonDTO

    Reads and returns a lesson by id

    It will fail if:
    a) HTTP 404; no lesson with provided id exists;

5)
    GET  /lesson
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  List<com.superum.api.lesson.ValidLessonDTO>

    Reads and returns a list of all lessons for given parameters;
    The parameters are evaluated as such:
    a) If start and end values are given, they are used;
    b) If any of them is not given, they are calculated from other parameters, then used;

    It shouldn't fail under normal circumstances;

    Returned List is paged; using DEF parameter values, only first 25 lessons will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a lesson is deleted, the results of this query will no longer be consistent with the ones before the deletion;

6)
    GET  /lesson/{tableName}/{id}
         tableName      String         teacher, customer, group or student
         id             long           1 <= id <= MAX_LONG
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  List<com.superum.api.lesson.ValidLessonDTO>

    Reads and returns a list of lessons for teacher, customer, group or student for given parameters;
    The parameters are evaluated as such:
    a) If start and end values are given, they are used;
    b) If any of them is not given, they are calculated from other parameters, then used;

    It will fail if:
    a) HTTP 404; no record for given table with provided id exists;

    Returned List is paged; using DEF parameter values, only first 25 lessons will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a lesson is deleted, the results of this query will no longer be consistent with the ones before the deletion;
