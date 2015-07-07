package com.superum.db.lesson.attendance;

import org.springframework.stereotype.Service;

@Service
public interface LessonAttendanceService {

	LessonAttendance addAttendanceToLesson(LessonAttendance attendance);
	
	LessonAttendance getAttendanceForLesson(long lessonId);
	
	LessonAttendance updateAttendanceForLesson(LessonAttendance attendance);
	
	LessonAttendance deleteAttendanceForLesson(long lessonId);
	
	LessonAttendance deleteAttendanceForLesson(LessonAttendance attendance);

	int deleteAttendanceForStudent(int studentId);
	
}
