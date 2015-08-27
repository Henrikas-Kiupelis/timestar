# Customer API:

## Relevant classes

[ValidCustomerDTO](https://github.com/Henrikas-Kiupelis/timestar/blob/master/src/main/java/com/superum/api/customer/ValidCustomerDTO.java)

    PUT   /customer
    BODY  ValidCustomerDTO
    RET   ValidCustomerDTO

Creates a new customer;

It will fail if:
* HTTP 400; the id field was set;
* HTTP 400; a mandatory field was not set;

Returned FullCustomer will have its id field set;

    GET  /customer/{customerId}
         customerId     int            1 <= customerId <= MAX_INT
    RET  com.superum.api.customer.ValidCustomerDTO

    Reads and returns an existing customer;

    It will fail if:
    a) HTTP 404; no customer with provided id exists;

3)
    POST  /customer
    BODY  com.superum.api.customer.ValidCustomerDTO
    RET   void

    Updates an existing customer; only fields that are sent are updated;

    It will fail if:
    a) HTTP 400; the id field was not set;
    b) HTTP 400; only the id field was set and no other fields were;
    c) HTTP 404; no customer with provided id exists;

    Returns HTTP 200 OK if it succeeds

4)
    DELETE  /customer/{customerId}
            customerId     int            1 <= customerId <= MAX_INT
    RET     void

    Deletes an existing customer;

    It will fail if:
    a) HTTP 404; no customer with provided id exists;

    Returns HTTP 200 OK if it succeeds

5)
    GET  /customer/teacher/{teacherId}
         teacherId      int            1 <= teacherId <= MAX_INT
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.customer.ValidCustomerDTO>

    Reads and returns a list of customers for a certain teacher;
    To determine if a certain customer is tied to a teacher, the following examination is made:
    1) customers have students;
    2) students are in groups;
    3) teachers are responsible for groups;
    4) so, a customer is tied to a teacher if they have any students in any groups that the teacher is responsible for;

    It will fail if:
    a) HTTP 404; no teacher with provided id exists;

    Returned List is paged; using DEF parameter values, only first 25 customers will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a customer is deleted, the results of this query will no longer be consistent with the ones before the deletion;

6)
    GET  /customer
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.customer.ValidCustomerDTO>

    Reads and returns a list of all customers;

    It shouldn't fail under normal circumstances;

    Returned List is paged; using DEF parameter values, only first 25 customers will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a customer is deleted, the results of this query will no longer be consistent with the ones before the deletion;

7)
    GET  /customer/teacher/{teacherId}/count
         teacherId      int            1 <= teacherId <= MAX_INT
    RET  int

    Counts and returns the amount of all customers for a certain teacher;
    To determine if a certain customer is tied to a teacher, the following examination is made:
    1) customers have students;
    2) students are in groups;
    3) teachers are responsible for groups;
    4) so, a customer is tied to a teacher if they have any students in any groups that the teacher is responsible for;

    It will fail if:
    a) HTTP 404; no teacher with provided id exists;

8)
    GET  /customer/count
    RET  int

    Counts and returns the amount of all customers;

    It shouldn't fail under normal circumstances;