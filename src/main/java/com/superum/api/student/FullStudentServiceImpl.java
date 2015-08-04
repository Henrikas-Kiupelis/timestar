package com.superum.api.student;

import com.superum.db.group.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FullStudentServiceImpl implements FullStudentService {

    @Override
    public List<Student> readAllStudentsForIds(List<Integer> studentIds, int partitionId) {
        return fullStudentDAO.read(studentIds, partitionId);
    }

    @Override
    public List<Student> readAllStudents(int partitionId) {
        return fullStudentDAO.readAll(partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public FullStudentServiceImpl(FullStudentDAO fullStudentDAO) {
        this.fullStudentDAO = fullStudentDAO;
    }

    // PRIVATE

    private final FullStudentDAO fullStudentDAO;

}
