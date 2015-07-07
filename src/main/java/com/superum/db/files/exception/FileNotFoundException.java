package com.superum.db.files.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such file found")
public class FileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3776978759347437614L;
	
	public FileNotFoundException() {
        super();
    }
    public FileNotFoundException(String message) {
        super(message);
    }
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public FileNotFoundException(Throwable cause) {
        super(cause);
    }

}
