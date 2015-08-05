package com.superum.api.teacher;

import org.springframework.stereotype.Repository;

@Repository
public interface FullTeacherDAO {

    int exists(FullTeacher customer, int partitionId);

}
