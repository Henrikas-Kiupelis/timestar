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
	
	List<Lesson> findLessonsForTeacher(int teacherId, Date start, Date end);
	
	List<Lesson> findLessonsForGroup(int groupId, Date start, Date end);
	
	List<Lesson> findLessonsForCustomer(int customerId, Date start, Date end);
	
}
