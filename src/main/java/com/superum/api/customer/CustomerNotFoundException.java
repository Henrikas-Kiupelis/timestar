package com.superum.api.customer;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when the a request for a Customer fails because the id used in the request
 * does not refer to any Customers
 * </pre>
 */
public class CustomerNotFoundException extends DatabaseException {

	public CustomerNotFoundException() {
		super();
	}

	public CustomerNotFoundException(String message) {
		super(message);
	}

	public CustomerNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CustomerNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
