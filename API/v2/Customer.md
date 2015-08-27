# Customer API

[Back to APIv2](./APIv2.md)

## Relevant classes

[ValidCustomerDTO](../../src/main/java/com/superum/api/customer/ValidCustomerDTO.java)

## Methods

### Commands

<a name="create"><a>
```
    PUT   /customer
    BODY  ValidCustomerDTO
    RET   ValidCustomerDTO
```

Creates a new customer

It will fail if:
* HTTP 400; the id field was set;
* HTTP 400; a mandatory field was not set;

Returned FullCustomer will have its id field set;

------

<a name="update"><a>
```
    POST  /customer
    BODY  ValidCustomerDTO
    RET   void
```

Updates an existing customer; only fields that are sent are updated

It will fail if:
* HTTP 400; the id field was not set;
* HTTP 400; only the id field was set and no other fields were;
* HTTP 404; no customer with provided id exists;

Returns HTTP 200 OK if it succeeds

------

<a name="delete"><a>
```
    DELETE  /customer/{customerId}
            customerId     int            1 <= customerId <= MAX_INT
    RET     void
```

Deletes an existing customer

It will fail if:
* HTTP 404; no customer with provided id exists;

Returns HTTP 200 OK if it succeeds

### Queries

<a name="read"><a>
```
    GET  /customer/{customerId}
         customerId     int            1 <= customerId <= MAX_INT
    RET  ValidCustomerDTO
```

Reads and returns an existing customer

It will fail if:
* HTTP 404; no customer with provided id exists;

------

<a name="read-for-teacher"><a>
```
    GET  /customer/teacher/{teacherId}
         teacherId      int            1 <= teacherId <= MAX_INT
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<ValidCustomerDTO>
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

<a name="read-all"><a>
```
    GET  /customer
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<ValidCustomerDTO>
```

Reads and returns a list of all customers

It shouldn't fail under normal circumstances

Returned List is paged; using DEF parameter values, only first 25 customers will be returned; to access the rest,
the page parameter must be incremented, or per_page value raised

------

<a name="count-for-teacher"><a>
```
    GET  /customer/teacher/{teacherId}/count
         teacherId      int            1 <= teacherId <= MAX_INT
    RET  int
```

Counts and returns the amount of all customers for a certain teacher;
To determine if a certain customer is tied to a teacher, the following examination is made:

1) customers have students;
2) students are in groups;
3) teachers are responsible for groups;
4) so, a customer is tied to a teacher if they have any students in any groups that the teacher is responsible for;

It will fail if:
* HTTP 404; no teacher with provided id exists;

------

<a name="count-all"><a>
```
    GET  /customer/count
    RET  int
```

Counts and returns the amount of all customers

It shouldn't fail under normal circumstances
