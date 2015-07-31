package com.superum.db.customer;

import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Override
	public Customer addCustomer(Customer customer, int partitionId) {
		LOG.debug("Creating new customer: {}", customer);
		
		Customer newCustomer = customerDAO.create(customer, partitionId);
		LOG.debug("New customer created: {}", newCustomer);
		
		return newCustomer;
	}
	
	@Override
	public Customer findCustomer(int id, int partitionId) {
		LOG.debug("Reading Customer by ID: {}", id);
		
		Customer customer = customerDAO.read(id, partitionId);
		LOG.debug("Customer retrieved: {}", customer);
		
		return customer;
	}
	
	@Override
	public Customer updateCustomer(Customer customer, int partitionId) {
		LOG.debug("Updating Customer: {}", customer);
		
		Customer oldCustomer = customerDAO.update(customer, partitionId);
		LOG.debug("Old Customer retrieved: {}", oldCustomer);
		
		return oldCustomer;
	}
	
	@Override
	public Customer deleteCustomer(int id, int partitionId) {
		LOG.debug("Deleting Customer by ID: {}", id);

		List<Student> deletedStudents = studentService.deleteForCustomer(id, partitionId);
		LOG.debug("Deleted Students for this Customer: {}", deletedStudents);
		
		Customer deletedCustomer = customerDAO.delete(id, partitionId);
		LOG.debug("Deleted Customer: {}", deletedCustomer);
		
		return deletedCustomer;
	}
	
	@Override
	public List<Customer> findCustomersForTeacher(int teacherId, int partitionId) {
		LOG.debug("Reading Customers for Teacher with ID: {}", teacherId);
		
		List<Customer> customersForTeacher = customerQueries.readAllForTeacher(teacherId, partitionId);
		LOG.debug("Customers retrieved: {}", customersForTeacher);
		
		return customersForTeacher;
	}
	
	@Override
	public List<Customer> getAllCustomers(int partitionId) {
		LOG.debug("Reading all Customers");
		
		List<Customer> allCustomers = customerDAO.readAll(partitionId);
		LOG.debug("Customers retrieved: {}", allCustomers);
		
		return allCustomers;
	}

	// CONSTRUCTORS
	
	@Autowired
	public CustomerServiceImpl(CustomerDAO customerDAO, CustomerQueries customerQueries, StudentService studentService) {
		this.customerDAO = customerDAO;
		this.customerQueries = customerQueries;
		this.studentService = studentService;
	}
	
	// PRIVATE
	
	private final CustomerDAO customerDAO;
	private final CustomerQueries customerQueries;
	private final StudentService studentService;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

}
