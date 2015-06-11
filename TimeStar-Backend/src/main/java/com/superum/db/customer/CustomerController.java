package com.superum.db.customer;

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
public class CustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Customer addNewCustomer(@RequestBody @Valid Customer teacher) {
		return customerService.addNewCustomer(teacher);
	}
	
	@RequestMapping(value = "/customer/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Customer findCustomer(@PathVariable int id) {
		return customerService.findCustomer(id);
	}
	
	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Customer updateCustomer(@RequestBody @Valid Customer teacher) {
		return customerService.updateCustomer(teacher);
	}
	
	@RequestMapping(value = "/customer/delete/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Customer deleteCustomer(@PathVariable int id) {
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
