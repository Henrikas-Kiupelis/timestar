package com.superum.db.lesson;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonQueries {

	List<Lesson> readAllForCustomer(int customerId, long start, long end, int partitionId);

}
