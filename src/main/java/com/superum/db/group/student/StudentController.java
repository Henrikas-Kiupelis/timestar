package com.superum.db.group.student;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class StudentController {

	@RequestMapping(value = "/student/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Student addStudent(Principal user, @RequestBody @Valid Student student) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.addStudent(student, partitionId);
	}
	
	@RequestMapping(value = "/student/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Student findStudent(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.findStudent(id, partitionId);
	}
	
	@RequestMapping(value = "/student/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Student updateStudent(Principal user, @RequestBody @Valid Student student) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.updateStudent(student, partitionId);
	}
	
	@RequestMapping(value = "/student/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Student deleteStudent(Principal user, @PathVariable int id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.deleteStudent(id, partitionId);
	}
	
	@RequestMapping(value = "/student/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Student> findStudentsForCustomer(Principal user, @PathVariable int customerId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.findStudentsForCustomer(customerId, partitionId);
	}
	
	@RequestMapping(value = "/student/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Student> findStudentsForGroup(Principal user, @PathVariable int groupId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.findStudentsForGroup(groupId, partitionId);
	}
	
	@RequestMapping(value = "/student/lesson/{lessonId:[d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Student> findStudentsForLesson(Principal user, @PathVariable long lessonId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return studentService.findStudentsForLesson(lessonId, partitionId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}
	
	// PRIVATE
	
	private final StudentService studentService;

}
