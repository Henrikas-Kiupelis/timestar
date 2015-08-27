Lesson table API:

1)
    GET   /lesson/table
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  com.superum.api.table.core.OptimizedLessonTableDTO

    Delegates the call to /lesson/table/1 using same parameters

2)
    GET  /lesson/table/{page}
         page           int            1 <= page <= MAX_INT
    OPT  per_page       int            1 <= page <= 12; DEF 6
    OPT  time_zone      String         any time zone; DEF UTC
    OPT  start_date     String         any Date; DEF today evaluated at time_zone
    OPT  end_date       String         any Date; DEF start_date
    OPT  start          long           0 <= page <= MAX_LONG; DEF start_date evaluated at 00:00:00 for time_zone
    OPT  end            long           0 <= page <= MAX_LONG; DEF (end_date + 1) evaluated at 00:00:00 for time_zone
    RET  com.superum.api.table.core.OptimizedLessonTableDTO

    Returns the lesson table for given parameters;
    The parameters are evaluated as such:
    a) If start and end values are given, they are used;
    b) If any of them is not given, they are calculated from other parameters, then used;

    It shouldn't fail under normal circumstances;
