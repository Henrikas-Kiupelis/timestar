# Lesson Table APIv3

[Back to APIv3](./APIv3.md)

## Info

[APIv2 version](../v2/LessonTable.md) - deprecated version

This extension was created for 2 reasons:
* optimize the lesson table even further
* separate parts of the lesson table so some of them can be cached by front end where appropriate

In the process of optimization, the call has been reduced to <100ms, essentially removing the need for separating the
parts; thus another, full version of the table was provided later on

The following optimizations have been made:

1. Group information is no longer retrieved; it is NOT visible on the table, and should be therefore AJAX'd instead;
2. The table is no longer formatted in back end; only calculated data is sent, that is:
  1. customerId
  2. teacherId
  3. lessons for those ids
  4. duration of those lessons
  5. cost of those lessons
  
  this means no more empty fields (i.e. with empty lesson id lists, 0 duration/cost) and thus less JSON serialization
  and less network bandwidth used; also, slightly reduced amount of calculations;
3. Sums of columns and rows no longer appear; since the data is not formatted anyway, it makes more sense to sum
in front end, because it's a simple operation (no domain logic needed here);
4. SQL queries have been simplified across the board; no more nested tables or the like. ~10-20ms per call!
(was ~30-40ms); not to mention the amount of calls has been also reduced (total of 5-6 calls per request!)

## Relevant classes

[Table](../../src/main/java/com/superum/v3/table/Table.java) - basic table; has teachers/customers/fields

[TableField](../../src/main/java/com/superum/v3/table/TableField.java) - contains the data found in one of the table's fields

[TableReport](../../src/main/java/com/superum/v3/table/TableReport.java) - contains a report on the amount required to pay and deadline

[FullTable](../../src/main/java/com/superum/v3/table/FullTable.java) - combines basic table and reports into one

### Commands

N/A

### Queries

<a name="table-size"><a>
```
    GET  /lesson/table/size
    RET  int
```

Returns the width of the table; in this case it refers to the amount of teachers in the system;
the call is delegated to [/v2/teacher/count](../v2/Teacher.md#count-all)

------

<a name="table-data-default"><a>
```
    GET  /lesson/table/data
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  FullTable
```

Delegates the call to [/lesson/table/data/1](#table-data) using same parameters

------

<a name="table-data"><a>
```
    GET  /lesson/table/data/{page}
         page           int            1 <= page <= MAX_INT
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  Table
```

Returns the lesson table for given parameters;
The parameters are evaluated as such:

1. If start and end values are given, they are used;
2. If any of them is not given, they are calculated from other parameters, then used;

It shouldn't fail under normal circumstances

------

<a name="table-report"><a>
```
    GET  /lesson/table/report/{source}
         source         String         teacher or customer
    REQ  id             String         comma separated list of ints representing ids: 1 <= id <= MAX_INT
    RET  List<TableReport>
```

Returns lesson table reports for teacher or customer with given ids

It will fail if:
* HTTP 404; teacher or customer for any of the provided ids doesn't exist;

------

<a name="table-data-full-default"><a>
```
    GET  /lesson/table/data/full
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  FullTable
```

Delegates the call to [/lesson/table/data/full/1](#table-data-full) using same parameters

------

<a name="table-data-full"><a>
```
    GET  /lesson/table/data/full/{page}
         page           int            1 <= page <= MAX_INT
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  FullTable
```

Returns the lesson table for given parameters;
The parameters are evaluated as such:

1. If start and end values are given, they are used;
2. If any of them is not given, they are calculated from other parameters, then used;

It shouldn't fail under normal circumstances

The returned table is a combination of [/lesson/table/data](#table-data) and 
both teacher and customer reports from [/lesson/table/report/{teacher|customer}](#table-report)
