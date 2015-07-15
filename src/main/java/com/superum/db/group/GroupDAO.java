package com.superum.db.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface GroupDAO extends SimplePartitionedDAO<Group, Integer> {
	
	List<Group> readAllForTeacher(int teacherId, int partitionId);
	
}
