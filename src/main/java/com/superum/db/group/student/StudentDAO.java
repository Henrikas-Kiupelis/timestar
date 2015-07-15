package com.superum.db.group.student;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface StudentDAO extends SimplePartitionedDAO<Student, Integer> {
	
	List<Student> readAllForGroup(int groupId, int partitionId);
	
	List<Student> readAllForCustomer(int customerId, int partitionId);

	List<Student> deleteAllForCustomer(int customerId, int partitionId);
	
}
