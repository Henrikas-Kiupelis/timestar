package com.superum.api.customer;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request to delete a customer fails because this customer still has
 * references in other database objects
 */
public class UnsafeCustomerDeleteException extends DatabaseException {

	public UnsafeCustomerDeleteException() {
		super();
	}

	public UnsafeCustomerDeleteException(String message) {
		super(message);
	}

	public UnsafeCustomerDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsafeCustomerDeleteException(Throwable cause) {
		super(cause);
	}
	
}
