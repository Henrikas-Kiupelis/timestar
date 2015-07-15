package com.superum.db.lesson.attendance.code;

import java.util.List;

import org.springframework.stereotype.Service;

import com.superum.db.lesson.attendance.LessonAttendance;

@Service
public interface LessonCodeService {

	List<LessonCode> add(LessonAttendance attendance, int partitionId);
	
	int verifyStudentId(long lessonId, int code, int partitionId);

	int deleteCodesForStudent(int studentId, int partitionId);

	int deleteCodesForLesson(long lessonId, int partitionId);
	
}
