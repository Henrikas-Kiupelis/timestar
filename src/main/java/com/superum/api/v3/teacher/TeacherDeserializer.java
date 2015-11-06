package com.superum.api.v3.teacher;

import com.superum.api.v3.teacher.dto.SuppliedTeacher;

public interface TeacherDeserializer {

    Teacher toCreatable(SuppliedTeacher fetchedTeacher);

    Teacher toUpdatable(SuppliedTeacher suppliedTeacher);

}
