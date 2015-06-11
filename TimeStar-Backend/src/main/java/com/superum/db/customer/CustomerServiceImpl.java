package com.superum.db.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		return customerDAO.delete(id);
	}

	// CONSTRUCTORS
	
	@Autowired
	public CustomerServiceImpl(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
	
	// PRIVATE
	
	private final CustomerDAO customerDAO;

}
