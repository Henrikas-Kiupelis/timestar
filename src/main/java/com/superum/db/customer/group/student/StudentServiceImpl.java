package com.superum.db.customer.group.student;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	// CONSTRUCTORS

	@Autowired
	public StudentServiceImpl(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}

	// PRIVATE
	
	private final StudentDAO studentDAO;

}
