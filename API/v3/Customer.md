# Customer APIv3

[Back to APIv3](./APIv3.md#api-v3)

## Info

Basically same as API v2, except uses different classes and allows all fields to be null

## Relevant classes

[SuppliedCustomer](../../src/main/java/com/superum/api/v3/customer/dto/SuppliedCustomer.java)
[FetchedCustomer](../../src/main/java/com/superum/api/v3/customer/dto/FetchedCustomer.java)

### Commands

#### Create
```
    POST  /customer
    BODY  SuppliedCustomer
    RET   FetchedCustomer
```

Creates a new customer

It will fail if:
  * HTTP 400; the id field was set;
  * HTTP 400; a mandatory field was not set;

Returned customer will have its id field set;

------

#### Update
```
    PUT   /customer
    BODY  SuppliedCustomer
    RET   void
```

Updates an existing customer; only fields that are sent are updated

It will fail if:
  * HTTP 400; the id field was not set;
  * HTTP 400; only the id field was set and no other fields were;
  * HTTP 404; no customer with provided id exists;

Returns HTTP 200 OK if it succeeds

------

#### Delete
```
    DELETE  /customer/{customerId}
            customerId     int            1 <= customerId <= MAX_INT
    RET     void
```

Deletes an existing customer

It will fail if:
  * HTTP 400; customer cannot be deleted because it is still used;
  * HTTP 404; no customer with provided id exists;

Returns HTTP 200 OK if it succeeds

### Queries

#### Read
```
    GET  /customer/{customerId}
         customerId     int            1 <= customerId <= MAX_INT
    RET  FetchedCustomer
```

Reads and returns an existing customer

It will fail if:
  * HTTP 404; no customer with provided id exists;

------

#### Read for teacher
```
    GET  /customer/teacher/{teacherId}
         teacherId      int            1 <= teacherId <= MAX_INT
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<FetchedCustomer>
```

Reads and returns a list of customers for a certain teacher;
To determine if a certain customer is tied to a teacher, the following examination is made:

1. customers have students;
2. students are in groups;
3. teachers are responsible for groups;
4. so, a customer is tied to a teacher if they have any students in any groups that the teacher is responsible for;

It will fail if:
  * HTTP 404; no teacher with provided id exists;

Returned List is paged; using DEF parameter values, only first 25 customers will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised

------

#### Read all
```
    GET  /customer
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<FetchedCustomer>
```

Reads and returns a list of all customers

It shouldn't fail under normal circumstances

Returned List is paged; using DEF parameter values, only first 25 customers will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised

------

#### Count for teacher
```
    GET  /customer/teacher/{teacherId}/count
         teacherId      int            1 <= teacherId <= MAX_INT
    RET  int
```

Counts and returns the amount of all customers for a certain teacher;
To determine if a certain customer is tied to a teacher, the following examination is made:

1. customers have students;
2. students are in groups;
3. teachers are responsible for groups;
4. so, a customer is tied to a teacher if they have any students in any groups that the teacher is responsible for;

It will fail if:
  * HTTP 404; no teacher with provided id exists;

------

#### Count all
```
    GET  /customer/count
    RET  int
```

Counts and returns the amount of all customers

It shouldn't fail under normal circumstances
