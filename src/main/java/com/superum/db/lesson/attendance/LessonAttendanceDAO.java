package com.superum.db.lesson.attendance;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimpleDAO;

@Repository
public interface LessonAttendanceDAO extends SimpleDAO<LessonAttendance, Long> {

	LessonAttendance delete(LessonAttendance attendance);

	int deleteForStudent(int studentId);
	
}
