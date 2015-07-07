package com.superum.db.lesson.attendance.code;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCodeDAO {

	List<LessonCode> add(List<LessonCode> lessonCodes);
	
	int find(long lessonId, int code);

	int deleteForStudent(int studentId);

	int deleteForLesson(long lessonId);
	
}
