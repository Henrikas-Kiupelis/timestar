package com.superum.db.customer;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	@RequestMapping(value = "/api/customer/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Customer addNewCustomer(@RequestBody @Valid Customer teacher) {
		return customerService.addNewCustomer(teacher);
	}
	
	@RequestMapping(value = "/api/customer/find", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Customer findCustomer(@RequestParam(value="id") int id) {
		return customerService.findCustomer(id);
	}
	
	@RequestMapping(value = "/api/customer/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Customer updateCustomer(@RequestBody @Valid Customer teacher) {
		return customerService.updateCustomer(teacher);
	}
	
	@RequestMapping(value = "/api/customer/delete", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Customer deleteCustomer(@RequestParam(value="id") int id) {
		return customerService.deleteCustomer(id);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	// PRIVATE
	
	private final CustomerService customerService;

}
