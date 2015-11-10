# Teacher APIv3

[Back to APIv3](./APIv3.md#api-v3)

## Info

Basically same as API v2, except uses different classes and allows all fields to be null

## Relevant classes

[SuppliedTeacher](../../src/main/java/com/superum/api/v3/teacher/dto/SuppliedTeacher.java)
[FetchedTeacher](../../src/main/java/com/superum/api/v3/teacher/dto/FetchedTeacher.java)

### Commands

#### Create
```
    POST  /teacher
    BODY  SuppliedTeacher
    RET   FetchedTeacher
```

Creates a new teacher;
also creates an Account for this teacher, with a randomly generated password, which is sent to the given e-mail
(only if email is provided)

It will fail if:
  * HTTP 400; the id field was set;
  * HTTP 400; a mandatory field was not set;
  * HTTP 409; the email is already taken in the partition of the request;

Returned Teacher will have its id field set;

------

#### Update
```
    PUT   /teacher
    BODY  SuppliedTeacher
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

#### Delete
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

#### Read
```
    GET  /teacher/{teacherId}
         teacherId      int            1 <= teacherId <= MAX_INT
    RET  FetchedTeacher
```

Reads and returns an existing teacher

It will fail if:
  * HTTP 404; no teacher with provided id exists;

------

#### Read all
```
    GET  /teacher
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<FetchedTeacher>
```

Reads and returns a list of all teachers

It shouldn't fail under normal circumstances

Returned List is paged; using DEF parameter values, only first 25 teachers will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised
    
------

#### Count all
```
    GET  /teacher/count
    RET  int
```

Counts and returns the amount of all teachers

It shouldn't fail under normal circumstances
