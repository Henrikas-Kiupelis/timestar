package com.superum.db.group.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.lesson.attendance.LessonAttendanceService;
import com.superum.db.lesson.attendance.code.LessonCodeService;

@Service
public class StudentServiceImpl implements StudentService {

	@Override
	public Student addStudent(Student student) {
		return studentDAO.create(student);
	}

	@Override
	public Student findStudent(int id) {
		return studentDAO.read(id);
	}

	@Override
	public Student updateStudent(Student student) {
		return studentDAO.update(student);
	}

	@Override
	public Student deleteStudent(int id) {
		attendanceService.deleteAttendanceForStudent(id);
		lessonCodeService.deleteCodesForStudent(id);		
		return studentDAO.delete(id);
	}

	@Override
	public List<Student> findStudentsForCustomer(int customerId) {
		return studentDAO.readAllForCustomer(customerId);
	}

	@Override
	public List<Student> findStudentsForGroup(int groupId) {
		return studentDAO.readAllForGroup(groupId);
	}
	
	@Override
	public List<Student> findStudentsForLesson(long lessonId) {
		return studentQueries.readAllForLesson(lessonId);
	}
	
	@Override
	public List<Student> deleteForCustomer(int customerId) {
		List<Student> old = findStudentsForCustomer(customerId);
		
		old.stream()
			.mapToInt(Student::getId)
			.forEach(this::deleteStudent);
		
		return old;
	}
	
	@Override
	public List<Student> deleteForGroup(int groupId) {
		List<Student> old = findStudentsForGroup(groupId);
		
		old.stream()
			.mapToInt(Student::getId)
			.forEach(this::deleteStudent);
		
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

}
