package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface LessonService {

	Lesson addLesson(Lesson lesson);
	
	Lesson findLesson(long id);
	
	Lesson updateLesson(Lesson lesson);
	
	Lesson deleteLesson(long id);
	
	List<Lesson> findLessonsFor(TableBinder table, int id, Date start, Date end);
	
}
