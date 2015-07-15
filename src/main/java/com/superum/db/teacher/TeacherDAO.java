package com.superum.db.teacher;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.PartitionedAccessDAO;
import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface TeacherDAO extends SimplePartitionedDAO<Teacher, Integer>, PartitionedAccessDAO<Teacher> {
	
	List<Teacher> readSome(int amount, int offset, int partitionId);
	
	int count(int partitionId);
	
}
