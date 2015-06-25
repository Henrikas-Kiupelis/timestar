package com.superum.db.lesson.table;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.customer.Customer;
import com.superum.db.customer.CustomerDAO;
import com.superum.db.lesson.table.core.CustomerLessonData;
import com.superum.db.lesson.table.core.LessonTable;
import com.superum.db.lesson.table.core.PaymentData;
import com.superum.db.lesson.table.core.TeacherLessonData;
import com.superum.db.lesson.table.core.TotalLessonData;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.TeacherDAO;

@Service
public class LessonTableServiceImpl implements LessonTableService {

	@Override
	public LessonTable lessonData(int amount, int offset, Date start, Date end) {
		List<Teacher> teachers = teacherDAO.readSome(amount, offset);
		
		List<CustomerLessonData> lessonData = customerDAO.readAll().stream()
				.map(customer -> customerData(customer, teachers, start, end))
				.collect(Collectors.toList());
				
		List<TotalLessonData> totalData = new ArrayList<>();
		for (int i = 0; i < teachers.size(); i++) {
			int count = 0;
			int duration = 0;
			BigDecimal cost = BigDecimal.ZERO;
			for (CustomerLessonData customerData : lessonData) {
				TeacherLessonData data = customerData.getLessonData().get(i);
				count += data.getLessonIds().size();
				duration += data.getDuration();
				cost = cost.add(data.getCost());
			}
		}
		
		List<PaymentData> paymentData;
		
		return new LessonTable(teachers, lessonData, totalData, paymentData);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableServiceImpl(TeacherDAO teacherDAO, CustomerDAO customerDAO, LessonTableQueries lessonTableQueries) {
		this.teacherDAO = teacherDAO;
		this.customerDAO = customerDAO;
		this.lessonTableQueries = lessonTableQueries;
	}

	// PRIVATE

	private final TeacherDAO teacherDAO;
	private final CustomerDAO customerDAO;
	private final LessonTableQueries lessonTableQueries;
	
	private CustomerLessonData customerData(Customer customer, List<Teacher> teachers, Date start, Date end) {
		List<TeacherLessonData> lessonData = teachers.stream()
				.map(teacher -> teacherData(teacher, customer, start, end))
				.collect(Collectors.toList());
		
		int count = 0;
		int duration = 0;
		BigDecimal cost = BigDecimal.ZERO;
		for (TeacherLessonData data : lessonData) {
			count += data.getLessonIds().size();
			duration += data.getDuration();
			cost = cost.add(data.getCost());
		}
		TotalLessonData totalData = new TotalLessonData(count, duration, cost);
		
		PaymentData paymentData;
		
		return new CustomerLessonData(customer, lessonData, totalData, paymentData);
	}
	
	private TeacherLessonData teacherData(Teacher teacher, Customer customer, Date start, Date end) {
		return lessonTableQueries.readForTeacherAndCustomer(teacher.getId(), customer.getId(), start, end);
	}

}
