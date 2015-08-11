package com.superum.db.lesson.table;

import com.superum.db.lesson.table.core.CustomerLanguages;
import com.superum.db.lesson.table.core.PaymentData;
import com.superum.db.lesson.table.core.TeacherLessonData;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonTableQueries {

	TeacherLessonData readForTeacherAndCustomer(int teacherId, int customerId, long start, long end, int partitionId);
	
	PaymentData countPriceForTeacher(int teacherId, int partitionId);
	
	PaymentData countPriceForCustomer(int customerId, int partitionId);

	CustomerLanguages getCustomerLanguages(int id, int partitionId);

}
