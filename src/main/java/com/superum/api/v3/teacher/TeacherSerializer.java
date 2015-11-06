package com.superum.api.v3.teacher;

import com.superum.api.v3.teacher.dto.FetchedTeacher;
import org.jooq.Record;

public interface TeacherSerializer {

    FetchedTeacher toReturnable(Record record);

}
