package com.superum.api.v3.teacher;

import com.superum.api.v3.teacher.dto.FetchedTeacher;
import com.superum.api.v3.teacher.dto.SuppliedTeacher;

public interface TeacherCommands {

    FetchedTeacher create(SuppliedTeacher suppliedTeacher);

    void update(SuppliedTeacher suppliedTeacher, int id);

    void delete(int id);

}
