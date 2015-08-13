package com.superum.api.account;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when the a request for a Account fails because the username used in the request
 * does not refer to any Accounts
 * </pre>
 */
public class AccountNotFoundException extends DatabaseException {

	public AccountNotFoundException() {
		super();
	}

	public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
