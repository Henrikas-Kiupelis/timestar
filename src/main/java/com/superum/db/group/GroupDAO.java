package com.superum.db.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface GroupDAO extends SimpleDAO<Group, Integer> {
	
	List<Group> readAllForTeacher(int teacherId);
	
}
