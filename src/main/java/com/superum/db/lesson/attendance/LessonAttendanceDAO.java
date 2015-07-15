package com.superum.db.lesson.attendance;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface LessonAttendanceDAO extends SimplePartitionedDAO<LessonAttendance, Long> {

	LessonAttendance delete(LessonAttendance attendance, int partitionId);

	int deleteForStudent(int studentId, int partitionId);
	
}
