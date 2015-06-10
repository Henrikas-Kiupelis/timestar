package com.superum.db.teacher;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeacherController {

	@RequestMapping(value = "/api/teacher/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Teacher addNewTeacher(@RequestBody @Valid Teacher teacher) {
		return teacherService.addNewTeacher(teacher);
	}
	
	@RequestMapping(value = "/api/teacher/find", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Teacher findTeacher(@RequestParam(value="id") int id) {
		return teacherService.findTeacher(id);
	}
	
	@RequestMapping(value = "/api/teacher/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Teacher updateTeacher(@RequestBody @Valid Teacher teacher) {
		return teacherService.updateTeacher(teacher);
	}
	
	@RequestMapping(value = "/api/teacher/delete", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Teacher deleteTeacher(@RequestParam(value="id") int id) {
		return teacherService.deleteTeacher(id);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}
	
	// PRIVATE
	
	private final TeacherService teacherService;

}
