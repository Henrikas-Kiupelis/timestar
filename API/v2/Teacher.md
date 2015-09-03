# Teacher API

[Back to APIv2](./APIv2.md#api-v2)

## Relevant classes

[FullTeacherDTO](../../src/main/java/com/superum/api/teacher/FullTeacherDTO.java)

### Commands

<a name="create"><a>
```
    PUT   /teacher
    BODY  FullTeacherDTO
    RET   FullTeacherDTO
```

Creates a new teacher;
also creates an Account for this teacher, with a randomly generated password, which is sent to the given e-mail

It will fail if:
  * HTTP 400; the id field was set;
  * HTTP 400; a mandatory field was not set;
  * HTTP 409; the email is already taken in the partition of the request;

Returned FullTeacher will have its id field set;

------

<a name="update"><a>
```
    POST  /teacher
    BODY  FullTeacherDTO
    RET   void
```

Updates an existing teacher; only fields that are sent are updated;

It will fail if:
  * HTTP 400; the id field was not set;
  * HTTP 400; only the id field was set and no other fields were;
  * HTTP 404; no teacher with provided id exists;
  * HTTP 409; the email is already taken in the partition of the request;

Returns HTTP 200 OK if it succeeds

------

<a name="delete"><a>
```
    DELETE  /teacher/{teacherId}
            teacherId      int            1 <= teacherId <= MAX_INT
    RET     void
```

Deletes an existing teacher;
also deletes the Account of this teacher

It will fail if:
  * HTTP 400; teacher cannot be deleted because it is still used;
  * HTTP 404; no teacher with provided id exists;

Returns HTTP 200 OK if it succeeds

### Queries

<a name="read"><a>
```
    GET  /teacher/{teacherId}
         teacherId      int            1 <= teacherId <= MAX_INT
    RET  FullTeacherDTO
```

Reads and returns an existing teacher

It will fail if:
  * HTTP 404; no teacher with provided id exists;

------

<a name="read-all"><a>
```
    GET  /teacher
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<FullTeacherDTO>
```

Reads and returns a list of all teachers

It shouldn't fail under normal circumstances

Returned List is paged; using DEF parameter values, only first 25 teachers will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised
    
------

<a name="count-all"><a>
```
    GET  /teacher/count
    RET  int
```

Counts and returns the amount of all teachers

It shouldn't fail under normal circumstances
