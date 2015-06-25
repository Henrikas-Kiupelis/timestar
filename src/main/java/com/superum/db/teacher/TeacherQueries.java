package com.superum.db.teacher;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface TeacherQueries {

	List<Teacher> readAllforLessons(Date start, Date end);
	
}
