package com.superum.db.lesson;

import com.superum.db.dao.SimplePartitionedDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonDAO extends SimplePartitionedDAO<Lesson, Long> {
	
	List<Lesson> readAllForGroup(int groupId, long start, long end, int partitionId);

	List<Lesson> readAllForTeacher(int teacherId, long start, long end, int partitionId);
	
	boolean isOverlapping(Lesson lesson, int partitionId);
	
}
