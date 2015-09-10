# Lesson API

[Back to APIv2](./APIv2.md#api-v2)

## Relevant classes

[ValidLessonDTO](../../src/main/java/com/superum/api/v2/lesson/ValidLessonDTO.java)

### Commands

<a name="create"><a>
```
    PUT   /lesson
    BODY  ValidLessonDTO
    RET   ValidLessonDTO
```

Creates a new lesson

It will fail if:
  * HTTP 400; the id field was set;
  * HTTP 400; a mandatory field was not set;
  * HTTP 404; no group with provided id exists;
  * HTTP 409; the lesson overlaps with another lesson for the teacher of the group this lesson is for;

Returned lesson will have its id field set

------

<a name="update"><a>
```
    POST  /lesson
    BODY  ValidLessonDTO
    RET   void
```

Updates a lesson

It will fail if:
  * HTTP 400; the id field was not set;
  * HTTP 400; only the id field was set and no other fields were;
  * HTTP 404; no lesson with provided id exists;
  * HTTP 404; no group with provided id exists;
  * HTTP 409; the lesson overlaps with another lesson for the teacher of the group this lesson is for;

Returns HTTP 200 OK if it succeeds

------

<a name="delete"><a>
```
    DELETE  /lesson/{lessonId}
            lessonId        long           1 <= lessonId <= MAX_LONG
    RET     void
```

Deletes a lesson

It will fail if:
  * HTTP 400; lesson cannot be deleted because it is still used;
  * HTTP 404; no lesson with provided id exists;

Returns HTTP 200 OK if it succeeds

### Queries

<a name="read"><a>
```
    GET  /lesson/{lessonId}
         lessonId        long           1 <= lessonId <= MAX_LONG
    RET  ValidLessonDTO
```

Reads and returns a lesson by id

It will fail if:
  * HTTP 404; no lesson with provided id exists;

------

<a name="read-all"><a>
```
    GET  /lesson
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  List<ValidLessonDTO>
```

Reads and returns a list of all lessons for given parameters;
The parameters are evaluated as such:

1. If start and end values are given, they are used;
2. If any of them is not given, they are calculated from other parameters, then used;

It shouldn't fail under normal circumstances

Returned List is paged; using DEF parameter values, only first 25 lessons will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised

------

<a name="read-for-table"><a>
```
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
    RET  List<ValidLessonDTO>
```

Reads and returns a list of lessons for teacher, customer, group or student for given parameters;
The parameters are evaluated as such:

1. If start and end values are given, they are used;
2. If any of them is not given, they are calculated from other parameters, then used;

It will fail if:
  * HTTP 404; no record for given table with provided id exists;

Returned List is paged; using DEF parameter values, only first 25 lessons will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised
