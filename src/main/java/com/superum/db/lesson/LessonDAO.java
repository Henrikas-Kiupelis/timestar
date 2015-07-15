package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.dao.SimplePartitionedDAO;

@Repository
public interface LessonDAO extends SimplePartitionedDAO<Lesson, Long> {

	List<Lesson> readAllForTeacher(int teacherId, Date start, Date end, int partitionId);
	
	List<Lesson> readAllForGroup(int groupId, Date start, Date end, int partitionId);
	
	boolean isOverlapping(Lesson lesson, int partitionId);
	
}
