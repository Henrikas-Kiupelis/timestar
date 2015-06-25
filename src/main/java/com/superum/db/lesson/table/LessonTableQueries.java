package com.superum.db.lesson.table;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.superum.db.lesson.table.core.TeacherLessonData;

@Repository
public interface LessonTableQueries {

	TeacherLessonData readForTeacherAndCustomer(int teacherId, int customerId, Date start, Date end);
	
	List<BigDecimal> countPriceForTeachers(List<Integer> teacherIds, Date start, Date end);
	
	BigDecimal countPriceForCustomer(int customerId, Date start, Date end);
	
}
