package com.superum.db.lesson.table;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.superum.db.teacher.lang.Languages;
import com.superum.db.teacher.lang.LanguagesDAO;

@Service
public class LessonTableServiceImpl implements LessonTableService {

	@Override
	public LessonTable lessonData(int amount, int offset, Date start, Date end) {
		LOG.debug("Retrieving lesson table.");
		
		List<Teacher> teachers = teacherDAO.readSome(amount, offset);
		LOG.debug("List of teachers: {}", teachers);
		
		List<Languages> languages = teachers.stream()
				.map(Teacher::getId)
				.map(languagesDAO::read)
				.collect(Collectors.toList());
		LOG.debug("List of languages: {}", languages);
		
		List<CustomerLessonData> lessonData = customerDAO.readAll().stream()
				.map(customer -> customerData(customer, teachers, start, end))
				.collect(Collectors.toList());
		LOG.debug("Customer data: {}", lessonData);
				
		List<TotalLessonData> totalData = new ArrayList<>();
		for (int i = 0; i < teachers.size(); i++) {
			int count = 0;
			int duration = 0;
			BigDecimal cost = BigDecimal.ZERO;
			for (CustomerLessonData customerData : lessonData) {
				TeacherLessonData data = customerData.getTeacherLessonData().get(i);
				count += data.getLessonIds().size();
				duration += data.getDuration();
				cost = cost.add(data.getCost());
			}
			totalData.add(new TotalLessonData(count, duration, cost));
		}
		LOG.debug("Total teacher data: {}", totalData);
		
		List<PaymentData> paymentData = teachers.stream()
				.mapToInt(Teacher::getId)
				.mapToObj(lessonTableQueries::countPriceForTeacher)
				.collect(Collectors.toList());
		LOG.debug("Teacher payment data: {}", paymentData);
		
		LessonTable lessonTable = new LessonTable(teachers, languages, lessonData, totalData, paymentData);
		LOG.debug("Full table: {}", lessonTable);
		
		return lessonTable;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableServiceImpl(TeacherDAO teacherDAO, LanguagesDAO languagesDAO, CustomerDAO customerDAO,
			LessonTableQueries lessonTableQueries) {
		
		this.teacherDAO = teacherDAO;
		this.languagesDAO = languagesDAO;
		this.customerDAO = customerDAO;
		this.lessonTableQueries = lessonTableQueries;
	}

	// PRIVATE

	private final TeacherDAO teacherDAO;
	private final LanguagesDAO languagesDAO;
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
		
		PaymentData paymentData = lessonTableQueries.countPriceForCustomer(customer.getId());
		
		return new CustomerLessonData(customer, lessonData, totalData, paymentData);
	}
	
	private TeacherLessonData teacherData(Teacher teacher, Customer customer, Date start, Date end) {
		return lessonTableQueries.readForTeacherAndCustomer(teacher.getId(), customer.getId(), start, end);
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonTableServiceImpl.class);

}
