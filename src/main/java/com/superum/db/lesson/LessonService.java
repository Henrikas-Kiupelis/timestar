package com.superum.db.lesson;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LessonService {

	Lesson addLesson(Lesson lesson, int partitionId);
	
	Lesson findLesson(long id, int partitionId);
	
	Lesson updateLesson(Lesson lesson, int partitionId);
	
	Lesson deleteLesson(long id, int partitionId);

	List<Lesson> findLessonsForTable(String table, int id, long start, long end, int partitionId);

	List<Lesson> deleteForTeacher(int teacherId, int partitionId);
	
	List<Lesson> deleteForGroup(int groupId, int partitionId);
	
}
