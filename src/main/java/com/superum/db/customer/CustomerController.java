package com.superum.db.customer;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.util.List;

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
public class CustomerController {

	@RequestMapping(value = "/customer/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer addCustomer(@RequestBody @Valid Customer customer) {
		return customerService.addCustomer(customer);
	}
	
	@RequestMapping(value = "/customer/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer findCustomer(@PathVariable int id) {
		return customerService.findCustomer(id);
	}
	
	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer updateCustomer(@RequestBody @Valid Customer customer) {
		return customerService.updateCustomer(customer);
	}
	
	@RequestMapping(value = "/customer/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Customer deleteCustomer(@PathVariable int id) {
		return customerService.deleteCustomer(id);
	}
	
	@RequestMapping(value = "/customer/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Customer> findCustomersForTeacher(@PathVariable int teacherId) {
		return customerService.findCustomersForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/customer/all", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	// PRIVATE
	
	private final CustomerService customerService;

}
