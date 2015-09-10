package com.superum.api.v2.files.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such file found")
public class FileNotFoundException extends RuntimeException {
	
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
