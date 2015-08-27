Student API:

1)
    PUT   /student
    BODY  com.superum.api.student.ValidStudentDTO
    RET   com.superum.api.student.ValidStudentDTO

    Creates a new student

    It will fail if:
    a) HTTP 400; the id field was set;
    b) HTTP 400; a mandatory field was not set;
	c) HTTP 400; both or neither of customerId and startDate were set;

    Returned student will have its id field set

2)
    POST  /student
    BODY  com.superum.api.student.ValidStudentDTO
    RET   void

    Updates a student

    It will fail if:
    a) HTTP 400; the id field was not set;
    b) HTTP 400; only the id field was set and no other fields were;
	c) HTTP 400; both or neither of customerId and startDate were set;
    d) HTTP 404; no student with provided id exists;

    Returns HTTP 200 OK if it succeeds

3)
    DELETE  /student/{studentId}
            studentId        int            1 <= studentId <= MAX_INT
    RET     void

    Deletes a student

    It will fail if:
    a) HTTP 400; student cannot be deleted because it is still used;
    a) HTTP 404; no student with provided id exists;

    Returns HTTP 200 OK if it succeeds

4)
    GET  /student/{studentId}
         studentId        int            1 <= studentId <= MAX_INT
    RET  com.superum.api.student.ValidStudentDTO

    Reads and returns a student by id

    It will fail if:
    a) HTTP 404; no student with provided id exists;

5)
    GET  /student
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.student.ValidStudentDTO>

    Reads and returns a list of all students

    It shouldn't fail under normal circumstances;

    Returned List is paged; using DEF parameter values, only first 25 students will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a student is deleted, the results of this query will no longer be consistent with the ones before the deletion;

6)
    GET  /student/{tableName}/{id}
         tableName      String         group, lesson or customer
         id             int            1 <= id <= MAX_INT
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.student.ValidStudentDTO>

    Reads and returns a list of students for group, lesson or customer

    It will fail if:
    a) HTTP 404; no group, lesson or customer with provided id exists;

    Returned List is paged; using DEF parameter values, only first 25 students will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a student is deleted, the results of this query will no longer be consistent with the ones before the deletion;
