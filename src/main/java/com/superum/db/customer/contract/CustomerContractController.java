package com.superum.db.customer.contract;

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
public class CustomerContractController {

	@RequestMapping(value = "/customer/contract/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContract addContract(@RequestBody @Valid CustomerContract contract) {
		return contractService.addContract(contract);
	}
	
	@RequestMapping(value = "/customer/contract/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContract findContract(@PathVariable int id) {
		return contractService.findContract(id);
	}
	
	@RequestMapping(value = "/customer/contract/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContract updateContract(@RequestBody @Valid CustomerContract contract) {
		return contractService.updateContract(contract);
	}
	
	@RequestMapping(value = "/customer/contract/delete/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public CustomerContract deleteContract(@PathVariable int id) {
		return contractService.deleteContract(id);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public CustomerContractController(CustomerContractService contractService) {
		this.contractService = contractService;
	}
	
	// PRIVATE
	
	private final CustomerContractService contractService;

}
