package com.superum.db.teacher;

import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TeacherQueries {

	List<Teacher> readAllForLessons(Date start, Date end, int partitionId);
	
}
