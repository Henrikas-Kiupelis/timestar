package com.superum.api.v3.teacher;

import com.superum.api.v3.teacher.dto.SuppliedTeacher;

public interface TeacherDeserializer {

    Teacher toCreatable(SuppliedTeacher suppliedTeacher);

    Teacher toUpdatable(SuppliedTeacher suppliedTeacher, int id);

}
