package com.superum.db.customer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.customer.contract.CustomerContract;
import com.superum.db.customer.contract.CustomerContractService;
import com.superum.db.customer.contract.lang.CustomerContractLanguages;
import com.superum.db.customer.contract.lang.CustomerContractLanguagesService;
import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Override
	public Customer addCustomer(Customer customer) {
		LOG.debug("Creating new customer: {}", customer);
		
		Customer newCustomer = customerDAO.create(customer);
		LOG.debug("New customer created: {}", newCustomer);
		
		return newCustomer;
	}
	
	@Override
	public Customer findCustomer(int id) {
		LOG.debug("Reading Customer by ID: {}", id);
		
		Customer customer = customerDAO.read(id);
		LOG.debug("Customer retrieved: {}", customer);
		
		return customer;
	}
	
	@Override
	public Customer updateCustomer(Customer customer) {
		LOG.debug("Updating Customer: {}", customer);
		
		Customer oldCustomer = customerDAO.update(customer);
		LOG.debug("Old Customer retrieved: {}", oldCustomer);
		
		return oldCustomer;
	}
	
	@Override
	public Customer deleteCustomer(int id) {
		LOG.debug("Deleting Customer by ID: {}", id);
		
		CustomerContract deletedContract = customerContractService.deleteContract(id);
		LOG.debug("Deleted CustomerContract for this Customer: {}", deletedContract);
		
		CustomerContractLanguages deletedLanguages = customerContractLanguagesService.deleteLanguagesForCustomerContract(id);
		LOG.debug("Deleted CustomerContractLanguages for this Customer: {}", deletedLanguages);
		
		List<Student> deletedStudents = studentService.deleteForCustomer(id);
		LOG.debug("Deleted Students for this Customer: {}", deletedStudents);
		
		Customer deletedCustomer = customerDAO.delete(id);
		LOG.debug("Deleted Customer: {}", deletedCustomer);
		
		return deletedCustomer;
	}
	
	@Override
	public List<Customer> findCustomersForTeacher(int teacherId) {
		LOG.debug("Reading Customers for Teacher with ID: {}", teacherId);
		
		List<Customer> customersForTeacher = customerQueries.readAllForTeacher(teacherId);
		LOG.debug("Customers retrieved: {}", customersForTeacher);
		
		return customersForTeacher;
	}
	
	@Override
	public List<Customer> getAllCustomers() {
		LOG.debug("Reading all Customers");
		
		List<Customer> allCustomers = customerDAO.readAll();
		LOG.debug("Customers retrieved: {}", allCustomers);
		
		return allCustomers;
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
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

}
