# API V2

[Back to README](/README.md#api-links)

## Links

### [Customer](./Customer.md#customer-api) [Group](./Group.md#group-api) [Lesson](./Lesson.md#lesson-api) [Student](./Student.md#student-api) [Teacher](./Teacher.md#teacher-api)

[Grouping/attendance](./Multi.md#groupingattendance-api) - for putting students into groups/describing which students attended a lesson

[LessonTable](./LessonTable.md#lesson-table-apiv2) - for the primary table of the application

[Miscellaneous](./Misc.md#misc-api) - for partitions, accounts, files and other misc stuff

## Description

First of all, the API is partitioned. This basically means that depending on the user who is accessing the API,
a different portion of the database will be used. All API calls are available only for the user's partition, with
the exception of the partition API itself.
Generally, this means that the username authorization is slightly more complicated (partition id must be added to the
auth header) - this might be changed at a later date - but for all intents and purposes, every partition functions
in exactly the same manner.

Secondly, all APIv2 calls will have the following prefix, unless stated otherwise:

    http://{IP}:{PORT}/timestar/api/v2/

The API might switch to HTTPS eventually.

All the methods will be given in this format (example):

    (1)    GET  /lesson/table/{pageId}
    (2)         pageId      int            0 < pageId <= MAX_INT
    (3)    OPT  per_page    int            0 < per_page <= 100; DEF 6
    (4)    OPT  start_date  java.sql.Date  any; DEF today
    (5)    OPT  end_date    java.sql.Date  any; DEF start_date
    (6)    RET  LessonTable

The relevant non-standard classes will be found at the top of the API page like this:
[LessonTable](../../src/main/java/com/superum/db/lesson/table/core/LessonTable.java);
there you can find the class documentation, which should have all the relevant JSON information.

`GET` refers to the HTTP method the request expects;
`/lesson/table` are constant mappings, which should always be in the request;
`/{pageId}` is a mutable mapping; it must also always be in the request, but just like a normal parameter,
    it can have different values and has certain limitations;
`(2)` shows what the limitations for `{pageId}` are; specifically, that it is an integer and must be positive;
`(3)` to `(5)` show parameters; `REQ` means that the parameter must be in the request, whereas `OPT` means that
this parameter can be skipped; finally, if the parameter is optional, then it WILL provide the default value
using the

    ; DEF

modifier; in this case, if `per_page` is not present, it defaults to 6; if `start_date` is not present, it defaults to
today's date; if `end_date` is not present, it defaults to `start_date`
(which will be today's date, if it also was not present)
`(6)` shows what kind of Class object is returned by the request, assuming it is successful; JSON format is used in all
methods, unless stated otherwise; please refer to the appropriate Class documentation
One more modifier exists:

    BODY

which means that the method expects HTTP Body part; it also specifies a particular Class, just like `RET`.

Finally, all API methods expect an auth header, unless stated otherwise;
Currently (2015-07-20) a BASIC auth header is used:

    Authentication: Basic base64(username:password)

It is important to note, that username takes the following form:

    partitionId.actualUsername

This allows same username/email/etc to be used in separate partitions;
This will be changed into a more secure scheme at a later date, when the basic functionality of the app has been
realized and somewhat tested.

Next, there will be a short description of the method, including:

* what the method does; if there's any special behaviour it is necessarily described here;
* when will the method fail, and what error response you can expect as a result; obvious failures,
    such as breaking the limitations of parameters or failing to provide required ones are omitted;
* the structure/meaning of the returned value; this can be described at the same time as method behaviour
    if it makes sense to do so
