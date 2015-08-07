package com.superum.db.teacher;

import com.superum.utils.PrincipalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherController {

	@RequestMapping(value = "/teacher/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher addTeacher(Principal user, @RequestBody @Valid Teacher teacher) {
		int partitionId = PrincipalUtils.partitionId(user);
		log.info("Request to add a new teacher: {}", teacher);
		
		Teacher addedTeacher = teacherService.addTeacher(teacher, partitionId);
		log.info("Teacher added: {}", addedTeacher);
		
		return addedTeacher;
	}
	
	@RequestMapping(value = "/teacher/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher findTeacher(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		log.info("Request to find a teacher with ID {}", id);
		
		Teacher teacher = teacherService.findTeacher(id, partitionId);
		log.info("Teacher found: {}", teacher);
		
		return teacher;
	}
	
	@RequestMapping(value = "/teacher/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher updateTeacher(Principal user, @RequestBody @Valid Teacher teacher) {
		int partitionId = PrincipalUtils.partitionId(user);
		log.info("Request to find update teacher: {}", teacher);
		
		Teacher outdatedTeacher = teacherService.updateTeacher(teacher, partitionId);
		log.info("Teacher before update: {}", outdatedTeacher);
		
		return outdatedTeacher;
	}
	
	@RequestMapping(value = "/teacher/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher deleteTeacher(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		log.info("Request to delete a teacher with ID {}", id);
		
		Teacher teacher = teacherService.deleteTeacher(id, partitionId);
		log.info("Teacher deleted: {}", teacher);
		
		return teacher;
	}
	
	@RequestMapping(value = "/teacher/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Teacher> getAllTeachers(Principal user) {
		int partitionId = PrincipalUtils.partitionId(user);
		return teacherService.getAllTeachers(partitionId);
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
