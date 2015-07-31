package com.superum.db.lesson;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface LessonService {

	Lesson addLesson(Lesson lesson, int partitionId);
	
	Lesson findLesson(long id, int partitionId);
	
	Lesson updateLesson(Lesson lesson, int partitionId);
	
	Lesson deleteLesson(long id, int partitionId);
	
	List<Lesson> findLessonsForTeacher(int teacherId, Date start, Date end, int partitionId);
	
	List<Lesson> findLessonsForGroup(int groupId, Date start, Date end, int partitionId);
	
	List<Lesson> findLessonsForCustomer(int customerId, Date start, Date end, int partitionId);

	List<Lesson> deleteForTeacher(int teacherId, int partitionId);
	
	List<Lesson> deleteForGroup(int groupId, int partitionId);
	
}
