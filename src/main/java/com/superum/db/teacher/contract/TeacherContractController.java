package com.superum.db.teacher.contract;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherContractController {

	@RequestMapping(value = "/teacher/contract/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherContract addContract(@RequestBody @Valid TeacherContract contract) {
		return contractService.addContract(contract);
	}
	
	@RequestMapping(value = "/teacher/contract/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherContract findContract(@PathVariable int teacherId) {
		return contractService.findContract(teacherId);
	}
	
	@RequestMapping(value = "/teacher/contract/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherContract updateContract(@RequestBody @Valid TeacherContract contract) {
		return contractService.updateContract(contract);
	}
	
	@RequestMapping(value = "/teacher/contract/delete/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherContract deleteContract(@PathVariable int teacherId) {
		return contractService.deleteContract(teacherId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public TeacherContractController(TeacherContractService contractService) {
		this.contractService = contractService;
	}
	
	// PRIVATE
	
	private final TeacherContractService contractService;

}
