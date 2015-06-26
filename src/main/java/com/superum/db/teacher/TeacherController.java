package com.superum.db.teacher;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherController {

	@RequestMapping(value = "/teacher/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Teacher addTeacher(@RequestBody @Valid Teacher teacher) {
		log.info("Request to add a new teacher: {}", teacher);
		
		Teacher addedTeacher = teacherService.addTeacher(teacher);
		log.info("Teacher added: {}", addedTeacher);
		
		return addedTeacher;
	}
	
	@RequestMapping(value = "/teacher/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Teacher findTeacher(@PathVariable int id) {
		log.info("Request to find a teacher with ID {}", id);
		
		Teacher teacher = teacherService.findTeacher(id);
		log.info("Teacher found: {}", teacher);
		
		return teacher;
	}
	
	@RequestMapping(value = "/teacher/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Teacher updateTeacher(@RequestBody @Valid Teacher teacher) {
		log.info("Request to find update teacher: {}", teacher);
		
		Teacher outdatedTeacher = teacherService.updateTeacher(teacher);
		log.info("Teacher before update: {}", outdatedTeacher);
		
		return outdatedTeacher;
	}
	
	@RequestMapping(value = "/teacher/delete/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Teacher deleteTeacher(@PathVariable int id) {
		log.info("Request to delete a teacher with ID {}", id);
		
		Teacher teacher = teacherService.deleteTeacher(id);
		log.info("Teacher deleted: {}", teacher);
		
		return teacher;
	}
	
	@RequestMapping(value = "/teacher/all", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Teacher> getAllTeachers() {
		return teacherService.getAllTeachers();
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}
	
	// PRIVATE
	
	private final TeacherService teacherService;
	
	private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

}
