package com.superum.db.teacher;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherController {

	@RequestMapping(value = "/teacher/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Teacher addNewTeacher(@RequestBody @Valid Teacher teacher) {
		return teacherService.addNewTeacher(teacher);
	}
	
	@RequestMapping(value = "/teacher/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Teacher findTeacher(@PathVariable int id) {
		return teacherService.findTeacher(id);
	}
	
	@RequestMapping(value = "/teacher/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Teacher updateTeacher(@RequestBody @Valid Teacher teacher) {
		return teacherService.updateTeacher(teacher);
	}
	
	@RequestMapping(value = "/teacher/delete/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Teacher deleteTeacher(@PathVariable int id) {
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
