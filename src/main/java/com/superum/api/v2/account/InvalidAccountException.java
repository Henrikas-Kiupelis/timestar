package com.superum.api.v2.account;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of Account
 * does not match any of the use cases
 */
public class InvalidAccountException extends DatabaseException {

	public InvalidAccountException() {
		super();
	}

	public InvalidAccountException(String message) {
		super(message);
	}

	public InvalidAccountException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAccountException(Throwable cause) {
		super(cause);
	}
	
}
