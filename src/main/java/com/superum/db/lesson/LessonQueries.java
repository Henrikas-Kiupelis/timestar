package com.superum.db.lesson;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LessonQueries {

	List<Lesson> readAllForCustomer(int customerId, Date start, Date end, int partitionId);

}
