package com.superum.db.lesson;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface LessonQueries {

	List<Lesson> readAllForCustomer(int customerId, Date start, Date end, int partitionId);
	
}
