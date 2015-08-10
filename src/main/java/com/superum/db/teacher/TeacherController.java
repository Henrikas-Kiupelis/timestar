package com.superum.db.teacher;

import com.superum.helper.PartitionAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherController {

	@RequestMapping(value = "/teacher/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher addTeacher(PartitionAccount account, @RequestBody @Valid Teacher teacher) {
		log.info("Request to add a new teacher: {}", teacher);
		
		Teacher addedTeacher = teacherService.addTeacher(teacher, account);
		log.info("Teacher added: {}", addedTeacher);
		
		return addedTeacher;
	}
	
	@RequestMapping(value = "/teacher/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher findTeacher(PartitionAccount account, @PathVariable int id) {
		log.info("Request to find a teacher with ID {}", id);
		
		Teacher teacher = teacherService.findTeacher(id, account.partitionId());
		log.info("Teacher found: {}", teacher);
		
		return teacher;
	}
	
	@RequestMapping(value = "/teacher/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher updateTeacher(PartitionAccount account, @RequestBody @Valid Teacher teacher) {
		log.info("Request to find update teacher: {}", teacher);
		
		Teacher outdatedTeacher = teacherService.updateTeacher(teacher, account.partitionId());
		log.info("Teacher before update: {}", outdatedTeacher);
		
		return outdatedTeacher;
	}
	
	@RequestMapping(value = "/teacher/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Teacher deleteTeacher(PartitionAccount account, @PathVariable int id) {
		log.info("Request to delete a teacher with ID {}", id);
		
		Teacher teacher = teacherService.deleteTeacher(id, account);
		log.info("Teacher deleted: {}", teacher);
		
		return teacher;
	}
	
	@RequestMapping(value = "/teacher/all", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Teacher> getAllTeachers(PartitionAccount account) {
		return teacherService.getAllTeachers(account.partitionId());
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
