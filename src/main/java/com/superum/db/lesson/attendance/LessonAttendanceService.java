package com.superum.db.lesson.attendance;

import org.springframework.stereotype.Service;

@Service
public interface LessonAttendanceService {

	LessonAttendance addAttendanceToLesson(LessonAttendance attendance, int partitionId);
	
	LessonAttendance getAttendanceForLesson(long lessonId, int partitionId);
	
	LessonAttendance updateAttendanceForLesson(LessonAttendance attendance, int partitionId);
	
	LessonAttendance deleteAttendanceForLesson(long lessonId, int partitionId);
	
	LessonAttendance deleteAttendanceForLesson(LessonAttendance attendance, int partitionId);

	int deleteAttendanceForStudent(int studentId, int partitionId);
	
}
