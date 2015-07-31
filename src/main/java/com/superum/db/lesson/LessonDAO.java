package com.superum.db.lesson;

import com.superum.db.dao.SimplePartitionedDAO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LessonDAO extends SimplePartitionedDAO<Lesson, Long> {
	
	List<Lesson> readAllForGroup(int groupId, Date start, Date end, int partitionId);

	List<Lesson> readAllForTeacher(int teacherId, Date start, Date end, int partitionId);
	
	boolean isOverlapping(Lesson lesson, int partitionId);
	
}
