package com.superum.db.lesson.attendance.code;

import java.util.List;

import org.springframework.stereotype.Service;

import com.superum.db.lesson.attendance.LessonAttendance;

@Service
public interface LessonCodeService {

	List<LessonCode> add(LessonAttendance attendance);
	
	int verifyStudentId(long lessonId, int code);

	int deleteCodesForStudent(int studentId);

	int deleteCodesForLesson(long lessonId);
	
}
