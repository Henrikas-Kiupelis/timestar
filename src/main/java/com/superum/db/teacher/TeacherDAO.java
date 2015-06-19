package com.superum.db.teacher;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface TeacherDAO extends SimpleDAO<Teacher, Integer> {

	List<Teacher> readAll();
	
}
