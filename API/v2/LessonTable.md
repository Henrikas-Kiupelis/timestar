# Lesson Table API

[Back to APIv2](./APIv2.md)

## Info

[APIv3 version](../v3/LessonTable.md) - updated/optimized version

This version was created as an attempt to optimize the table while retaining the general structure of JSON requests;
also, changes such as checking for hourly vs academic wages for groups was implemented; unfortunately, this version
is still quite slow, because some queries happen too many times, prompting the creation of an even more updated version;

## Relevant classes

[OptimizedLessonTableDTO](../../src/main/java/com/superum/api/table/dto/OptimizedLessonTableDTO.java) - represents the entire table

[CustomerLessonDataDTO](../../src/main/java/com/superum/api/table/dto/CustomerLessonDataDTO.java) - represents a row of a table; belongs to a customer

[TeacherLessonDataDTO](../../src/main/java/com/superum/api/table/dto/TeacherLessonDataDTO.java) - represents a field of the table; belongs to a customer and a teacher

[TotalLessonDataDTO](../../src/main/java/com/superum/api/table/dto/TotalLessonDataDTO.java) - represents sum of a row or column

[PaymentDataDTO](../../src/main/java/com/superum/api/table/dto/PaymentDataDTO.java) - represents a report on customer or teacher payments (when and how much needs to be done)

### Commands

N/A

### Queries

<a name="table-default"><a>
```
    GET   /lesson/table
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  OptimizedLessonTableDTO
```

Delegates the call to [/lesson/table/1](#table) using same parameters
    
------

<a name="table"><a>
```
    GET  /lesson/table/{page}
         page           int            1 <= page <= MAX_INT
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  OptimizedLessonTableDTO
```

Returns the lesson table for given parameters;
The parameters are evaluated as such:

1. If start and end values are given, they are used;
2. If any of them is not given, they are calculated from other parameters, then used;

It shouldn't fail under normal circumstances;
