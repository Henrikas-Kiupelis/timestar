package com.superum.db.group.student;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentQueries {
	
	List<Student> readAllForLesson(long lessonId, int partitionId);

	List<Student> readAllForGroup(int groupId, int partitionId);
	
}
