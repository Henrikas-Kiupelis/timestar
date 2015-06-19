package com.superum.db.teacher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

	@Override
	public Teacher addTeacher(Teacher teacher) {
		return teacherDAO.create(teacher);
	}
	
	@Override
	public Teacher findTeacher(int id) {
		return teacherDAO.read(id);
	}
	
	@Override
	public Teacher updateTeacher(Teacher teacher) {
		return teacherDAO.update(teacher);
	}
	
	@Override
	public Teacher deleteTeacher(int id) {
		return teacherDAO.delete(id);
	}
	
	@Override
	public List<Teacher> getAllTeachers() {
		return teacherDAO.readAll();
	}

	// CONSTRUCTORS
	
	@Autowired
	public TeacherServiceImpl(TeacherDAO teacherDAO) {
		this.teacherDAO = teacherDAO;
	}
	
	// PRIVATE
	
	private final TeacherDAO teacherDAO;

}
