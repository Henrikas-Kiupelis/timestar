package com.superum.db.group.student;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class StudentController {

	@RequestMapping(value = "/student/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Student addStudent(PartitionAccount account, @RequestBody @Valid Student student) {
		return studentService.addStudent(student, account.partitionId());
	}
	
	@RequestMapping(value = "/student/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Student findStudent(PartitionAccount account, @PathVariable int id) {
		return studentService.findStudent(id, account.partitionId());
	}
	
	@RequestMapping(value = "/student/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Student updateStudent(PartitionAccount account, @RequestBody @Valid Student student) {
		return studentService.updateStudent(student, account.partitionId());
	}
	
	@RequestMapping(value = "/student/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Student deleteStudent(PartitionAccount account, @PathVariable int id) {
		return studentService.deleteStudent(id, account.partitionId());
	}
	
	@RequestMapping(value = "/student/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Student> findStudentsForCustomer(PartitionAccount account, @PathVariable int customerId) {
		return studentService.findStudentsForCustomer(customerId, account.partitionId());
	}
	
	@RequestMapping(value = "/student/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Student> findStudentsForGroup(PartitionAccount account, @PathVariable int groupId) {
		return studentService.findStudentsForGroup(groupId, account.partitionId());
	}
	
	@RequestMapping(value = "/student/lesson/{lessonId:[d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Student> findStudentsForLesson(PartitionAccount account, @PathVariable long lessonId) {
		return studentService.findStudentsForLesson(lessonId, account.partitionId());
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}
	
	// PRIVATE
	
	private final StudentService studentService;

}
