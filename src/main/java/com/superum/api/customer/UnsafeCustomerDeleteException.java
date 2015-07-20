package com.superum.api.customer;

import com.superum.exception.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * This exception is thrown when the a request to delete a Customer fails because this Customer still has
 * references in other database objects
 * </pre>
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Cannot delete the specified Customer because it is still being used")
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
