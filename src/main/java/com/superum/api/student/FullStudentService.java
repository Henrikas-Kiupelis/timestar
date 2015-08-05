package com.superum.api.student;

import com.superum.db.group.student.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FullStudentService {

    List<Student> readAllStudentsForIds(List<Integer> studentIds, int partitionId);

    List<Student> readAllStudents(int partitionId);

}
