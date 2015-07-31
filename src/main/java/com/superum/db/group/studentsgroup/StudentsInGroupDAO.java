package com.superum.db.group.studentsgroup;

import com.superum.db.dao.SimpleManyToManyPartitionedDAO;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentsInGroupDAO extends SimpleManyToManyPartitionedDAO<Integer, Integer> {

    int deleteForStudent(int studentId, int partitionId);

}
