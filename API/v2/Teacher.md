Teacher API:

1)
    PUT   /teacher
    BODY  com.superum.api.teacher.FullTeacher
    RET   com.superum.api.teacher.FullTeacher

    Creates a new teacher;
    also creates an Account for this teacher, with a randomly generated password, which is sent to the e-mail
    specified inside the FullTeacher

    It will fail if:
    a) HTTP 400; the id field was set;
    b) HTTP 400; a mandatory field was not set;
    c) HTTP 409; the email is already taken in the partition of the request;

    Returned FullTeacher will have its id field set;

2)
    GET  /teacher/{teacherId}
         teacherId      int            1 <= teacherId <= MAX_INT
    RET  com.superum.api.teacher.FullTeacher

    Reads and returns an existing teacher;

    It will fail if:
    a) HTTP 404; no teacher with provided id exists;

3)
    POST  /teacher
    BODY  com.superum.api.teacher.FullTeacher
    RET   void

    Updates an existing teacher; only fields that are sent are updated;

    It will fail if:
    a) HTTP 400; the id field was not set;
    b) HTTP 400; only the id field was set and no other fields were;
    c) HTTP 404; no teacher with provided id exists;
    d) HTTP 409; the email is already taken in the partition of the request;

    Returns HTTP 200 OK if it succeeds

4)
    DELETE  /teacher/{teacherId}
            teacherId      int            1 <= teacherId <= MAX_INT
    RET     void

    Deletes an existing teacher;
    also deletes the Account of this teacher;

    It will fail if:
    a) HTTP 404; no teacher with provided id exists;

    Returns HTTP 200 OK if it succeeds

5)
    GET  /teacher
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.teacher.FullTeacher>

    Reads and returns a list of all teachers;

    It shouldn't fail under normal circumstances;

    Returned List is paged; using DEF parameter values, only first 25 teachers will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a teacher is deleted, the results of this query will no longer be consistent with the ones before the deletion;

6)
    GET  /teacher/count
    RET  int

    Counts and returns the amount of all teachers;

    It shouldn't fail under normal circumstances;
