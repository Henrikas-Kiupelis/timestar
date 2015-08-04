package com.superum.api.student;

import com.superum.db.group.student.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FullStudentDAO {

    List<Student> read(List<Integer> studentIds, int partitionId);

    List<Student> readAll(int partitionId);

}
