# Lesson Table API

## Relevant classes

[Table](../../src/main/java/com/superum/v3/table/Table.java) - basic table; has teachers/customers/fields

[TableField](../../src/main/java/com/superum/v3/table/TableField.java) - contains the data found in one of the table's fields

[TableReport](../../src/main/java/com/superum/v3/table/TableReport.java) - contains a report on the amount required to pay and deadline

[FullTable](../../src/main/java/com/superum/v3/table/FullTable.java) - combines basic table and reports into one

## Methods

### Commands

N/A

### Queries

```
    GET  /lesson/table/size
    RET  int
```

Returns the width of the table; in this case it refers to the amount of teachers in the system;
the call is delegated to [/v2/teacher/count](../v2/Customer.md#queries)

------

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

------

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

------

```
    GET  /lesson/table/report/{source}
         source         String         teacher or customer
    REQ  id             String         comma separated list of ints representing ids: 1 <= id <= MAX_INT
    RET  List<TableReport>
```

------

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

------

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


