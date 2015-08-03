package com.superum.db.group.student;

import com.superum.db.dao.SimplePartitionedDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDAO extends SimplePartitionedDAO<Student, Integer> {
	
	List<Student> readAllForCustomer(int customerId, int partitionId);

	List<Student> deleteAllForCustomer(int customerId, int partitionId);

	int setStudentCode(int studentId, int code, int partitionId);
	
}
