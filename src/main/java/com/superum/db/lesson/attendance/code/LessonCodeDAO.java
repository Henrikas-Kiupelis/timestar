package com.superum.db.lesson.attendance.code;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonCodeDAO {

	List<LessonCode> add(List<LessonCode> lessonCodes, int partitionId);
	
	int find(long lessonId, int code, int partitionId);

	int deleteForStudent(int studentId, int partitionId);

	int deleteForLesson(long lessonId, int partitionId);
	
}
