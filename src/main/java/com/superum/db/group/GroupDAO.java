package com.superum.db.group;

import com.superum.db.dao.SimplePartitionedDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDAO extends SimplePartitionedDAO<Group, Integer> {
	
	List<Group> readAllForTeacher(int teacherId, int partitionId);

	List<Group> all(int partitionId);
	
}
