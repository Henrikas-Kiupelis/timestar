package com.superum.api.teacher;

import org.springframework.stereotype.Repository;

@Repository
public interface FullTeacherDAO {

    Integer exists(FullTeacher customer, int partitionId);

}
