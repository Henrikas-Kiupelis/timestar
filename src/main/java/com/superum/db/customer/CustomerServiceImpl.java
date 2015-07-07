package com.superum.db.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.customer.contract.CustomerContractService;
import com.superum.db.customer.contract.lang.CustomerContractLanguagesService;
import com.superum.db.group.student.StudentService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Override
	public Customer addCustomer(Customer customer) {
		return customerDAO.create(customer);
	}
	
	@Override
	public Customer findCustomer(int id) {
		return customerDAO.read(id);
	}
	
	@Override
	public Customer updateCustomer(Customer customer) {
		return customerDAO.update(customer);
	}
	
	@Override
	public Customer deleteCustomer(int id) {
		customerContractService.deleteContract(id);
		customerContractLanguagesService.deleteLanguagesForCustomerContract(id);
		studentService.deleteForCustomer(id);
		
		return customerDAO.delete(id);
	}
	
	@Override
	public List<Customer> findCustomersForTeacher(int teacherId) {
		return customerQueries.readAllForTeacher(teacherId);
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		return customerDAO.readAll();
	}

	// CONSTRUCTORS
	
	@Autowired
	public CustomerServiceImpl(CustomerDAO customerDAO, CustomerQueries customerQueries,
			CustomerContractService customerContractService, CustomerContractLanguagesService customerContractLanguagesService, StudentService studentService) {
		this.customerDAO = customerDAO;
		this.customerQueries = customerQueries;
		this.customerContractService = customerContractService;
		this.customerContractLanguagesService = customerContractLanguagesService;
		this.studentService = studentService;
	}
	
	// PRIVATE
	
	private final CustomerDAO customerDAO;
	private final CustomerQueries customerQueries;
	private final CustomerContractService customerContractService;
	private final CustomerContractLanguagesService customerContractLanguagesService;
	private final StudentService studentService;

}
