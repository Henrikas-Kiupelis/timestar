package com.superum.db.group.student;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface StudentQueries {
	
	List<Student> readAllForLesson(long lessonId, int partitionId);
	
}
