package com.superum.db.lesson.table;

import com.superum.db.customer.Customer;
import com.superum.db.customer.CustomerDAO;
import com.superum.db.lesson.table.core.*;
import com.superum.db.teacher.Teacher;
import com.superum.db.teacher.TeacherDAO;
import com.superum.db.teacher.lang.TeacherLanguages;
import com.superum.db.teacher.lang.TeacherLanguagesDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LessonTableServiceImpl implements LessonTableService {

	@Override
	public LessonTable lessonData(int amount, int offset, long start, long end, int partitionId) {
		LOG.debug("Retrieving lesson table.");
		
		int totalTeacherCount = teacherDAO.count(partitionId);
		LOG.debug("Total amount of teachers: {}", totalTeacherCount);
		
		List<Teacher> teachers = teacherDAO.readSome(amount, offset, partitionId);
		LOG.debug("List of teachers: {}", teachers);
		
		List<TeacherLanguages> languages = teachers.stream()
				.map(Teacher::getId)
				.map(teacherId -> languagesDAO.read(teacherId, partitionId))
				.collect(Collectors.toList());
		LOG.debug("List of languages: {}", languages);
		
		List<CustomerLessonData> lessonData = customerDAO.readAll(partitionId).stream()
				.map(customer -> customerData(customer, teachers, start, end, partitionId))
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
				.mapToObj(teacherId -> lessonTableQueries.countPriceForTeacher(teacherId, partitionId))
				.collect(Collectors.toList());
		LOG.debug("Teacher payment data: {}", paymentData);
		
		LessonTable lessonTable = new LessonTable(totalTeacherCount, teachers, languages, lessonData, totalData, paymentData);
		LOG.debug("Full table: {}", lessonTable);
		
		return lessonTable;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableServiceImpl(TeacherDAO teacherDAO, TeacherLanguagesDAO languagesDAO, CustomerDAO customerDAO, LessonTableQueries lessonTableQueries) {
		this.teacherDAO = teacherDAO;
		this.languagesDAO = languagesDAO;
		this.customerDAO = customerDAO;
		this.lessonTableQueries = lessonTableQueries;
	}

	// PRIVATE

	private final TeacherDAO teacherDAO;
	private final TeacherLanguagesDAO languagesDAO;
	private final CustomerDAO customerDAO;
	private final LessonTableQueries lessonTableQueries;
	
	private CustomerLessonData customerData(Customer customer, List<Teacher> teachers, long start, long end, int partitionId) {
		CustomerLanguages contractLanguages = lessonTableQueries.getCustomerLanguages(customer.getId(), partitionId);
		
		List<TeacherLessonData> lessonData = teachers.stream()
				.map(teacher -> teacherData(teacher, customer, start, end, partitionId))
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
		
		PaymentData paymentData = lessonTableQueries.countPriceForCustomer(customer.getId(), partitionId);
		
		return new CustomerLessonData(customer, contractLanguages, lessonData, totalData, paymentData);
	}
	
	private TeacherLessonData teacherData(Teacher teacher, Customer customer, long start, long end, int partitionId) {
		return lessonTableQueries.readForTeacherAndCustomer(teacher.getId(), customer.getId(), start, end, partitionId);
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonTableServiceImpl.class);

}
