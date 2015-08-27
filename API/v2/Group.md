Group API:

1)
    PUT   /group
    BODY  com.superum.api.group.ValidGroupDTO
    RET   com.superum.api.group.ValidGroupDTO

    Creates a new group

    It will fail if:
    a) HTTP 400; the id field was set;
    b) HTTP 400; a mandatory field was not set;

    Returned group will have its id field set

2)
    POST  /group
    BODY  com.superum.api.group.ValidGroupDTO
    RET   void

    Updates a group

    It will fail if:
    a) HTTP 400; the id field was not set;
    b) HTTP 400; only the id field was set and no other fields were;
    c) HTTP 404; no group with provided id exists;

    Returns HTTP 200 OK if it succeeds

3)
    DELETE  /group/{groupId}
            groupId        int            1 <= groupId <= MAX_INT
    RET     void

    Deletes a group

    It will fail if:
    a) HTTP 400; group cannot be deleted because it is still used;
    a) HTTP 404; no group with provided id exists;

    Returns HTTP 200 OK if it succeeds

4)
    GET  /group/{groupId}
         groupId        int            1 <= groupId <= MAX_INT
    RET  com.superum.api.group.ValidGroupDTO

    Reads and returns a group by id

    It will fail if:
    a) HTTP 404; no group with provided id exists;

5)
    GET  /group
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.group.ValidGroupDTO>

    Reads and returns a list of all groups

    It shouldn't fail under normal circumstances;

    Returned List is paged; using DEF parameter values, only first 25 groups will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a group is deleted, the results of this query will no longer be consistent with the ones before the deletion;

6)
    GET  /group/{tableName}/{id}
         tableName      String         teacher, customer or student
         id             int            1 <= id <= MAX_INT
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.group.ValidGroupDTO>

    Reads and returns a list of groups for teacher, customer or student

    It will fail if:
    a) HTTP 404; no teacher, customer or student with provided id exists;

    Returned List is paged; using DEF parameter values, only first 25 groups will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a group is deleted, the results of this query will no longer be consistent with the ones before the deletion;

7)
    GET  /customer/none
    OPT  page           int            1 <= page <= MAX_INT; DEF 1
    OPT  per_page       int            1 <= per_page <= 100; DEF 25
    RET  List<com.superum.api.group.ValidGroupDTO>

    Reads and returns a list of groups that have no customer

    It shouldn't fail under normal circumstances;

    Returned List is paged; using DEF parameter values, only first 25 groups will be returned; to access the rest,
    the page parameter must be incremented, or per_page value raised;
    If a group is deleted, the results of this query will no longer be consistent with the ones before the deletion;
