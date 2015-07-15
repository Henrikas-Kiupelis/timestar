package com.superum.db.group.student;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.lesson.attendance.LessonAttendanceService;
import com.superum.db.lesson.attendance.code.LessonCodeService;

@Service
public class StudentServiceImpl implements StudentService {

	@Override
	public Student addStudent(Student student, int partitionId) {
		LOG.debug("Creating new Student: {}", student);
		
		Student newStudent = studentDAO.create(student, partitionId);
		LOG.debug("New Student created: {}", newStudent);
		
		return newStudent;
	}

	@Override
	public Student findStudent(int id, int partitionId) {
		LOG.debug("Reading Student by ID: {}", id);
		
		Student student = studentDAO.read(id, partitionId);
		LOG.debug("Student retrieved: {}");
		
		return student;
	}

	@Override
	public Student updateStudent(Student student, int partitionId) {
		LOG.debug("Updating Student: {}", student);
		
		Student oldStudent = studentDAO.update(student, partitionId);
		LOG.debug("Old Student retrieved: {}", oldStudent);
		
		return oldStudent;
	}

	@Override
	public Student deleteStudent(int id, int partitionId) {
		LOG.debug("Deleting Student by ID: {}", id);
		
		int deletedAttendanceCount = attendanceService.deleteAttendanceForStudent(id, partitionId);
		LOG.debug("Deleted {} Student attendance records", deletedAttendanceCount);
		
		int deletedCodeCount = lessonCodeService.deleteCodesForStudent(id, partitionId);		
		LOG.debug("Deleted {} Student lesson codes", deletedCodeCount);
		
		Student deletedStudent = studentDAO.delete(id, partitionId);
		LOG.debug("Deleted Student: {}", deletedStudent);
		
		return deletedStudent;
	}

	@Override
	public List<Student> findStudentsForCustomer(int customerId, int partitionId) {
		LOG.debug("Reading Students for Customer with ID: {}", customerId);
		
		List<Student> studentsForCustomer = studentDAO.readAllForCustomer(customerId, partitionId);
		LOG.debug("Students retrieved: {}", studentsForCustomer);
		
		return studentsForCustomer;
	}

	@Override
	public List<Student> findStudentsForGroup(int groupId, int partitionId) {
		LOG.debug("Reading Students for Group with ID: {}", groupId);
		
		List<Student> studentsForGroup = studentDAO.readAllForGroup(groupId, partitionId);
		LOG.debug("Students retrieved: {}", studentsForGroup);
		
		return studentsForGroup;
	}
	
	@Override
	public List<Student> findStudentsForLesson(long lessonId, int partitionId) {
		LOG.debug("Reading Students for Lesson with ID: {}", lessonId);
		
		List<Student> studentsForLesson = studentQueries.readAllForLesson(lessonId, partitionId);
		LOG.debug("Students retrieved: {}", studentsForLesson);
		
		return studentsForLesson;
	}
	
	@Override
	public List<Student> deleteForCustomer(int customerId, int partitionId) {
		LOG.debug("Deleting Students for Customer with ID: {}", customerId);
		
		List<Student> old = findStudentsForCustomer(customerId, partitionId);
		
		old.stream()
			.mapToInt(Student::getId)
			.forEach(studentId -> deleteStudent(studentId, partitionId));
		LOG.debug("Deleted Students: {}", old);
		
		return old;
	}
	
	@Override
	public List<Student> deleteForGroup(int groupId, int partitionId) {
		LOG.debug("Deleting Students for Group with ID: {}", groupId);
		
		List<Student> old = findStudentsForGroup(groupId, partitionId);
		
		old.stream()
			.mapToInt(Student::getId)
			.forEach(studentId -> deleteStudent(studentId, partitionId));
		LOG.debug("Deleted Students: {}", old);
		
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public StudentServiceImpl(StudentDAO studentDAO, StudentQueries studentQueries, LessonAttendanceService attendanceService, LessonCodeService lessonCodeService) {
		this.studentDAO = studentDAO;
		this.studentQueries = studentQueries;
		this.attendanceService = attendanceService;
		this.lessonCodeService = lessonCodeService;
	}

	// PRIVATE
	
	private final StudentDAO studentDAO;
	private final StudentQueries studentQueries;
	private final LessonAttendanceService attendanceService;
	private final LessonCodeService lessonCodeService;
	
	private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

}
