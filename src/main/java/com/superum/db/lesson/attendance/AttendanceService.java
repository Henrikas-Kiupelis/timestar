package com.superum.db.lesson.attendance;

import org.springframework.stereotype.Service;

@Service
public interface AttendanceService {

	Attendance addAttendanceToLesson(Attendance attendance);
	
	Attendance getAttendanceForLesson(long lessonId);
	
	Attendance updateAttendanceForLesson(Attendance attendance);
	
	Attendance deleteAttendanceForLesson(long lessonId);
	
	Attendance deleteAttendanceForLesson(Attendance attendance);
	
}
