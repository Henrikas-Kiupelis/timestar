package com.superum.api.attendance;

import com.superum.api.exception.InvalidRequestException;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of lesson attendance
 * does not match any of the use cases
 * </pre>
 */
public class InvalidLessonAttendanceException extends InvalidRequestException {

	public InvalidLessonAttendanceException() {
		super();
	}

	public InvalidLessonAttendanceException(String message) {
		super(message);
	}

	public InvalidLessonAttendanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLessonAttendanceException(Throwable cause) {
		super(cause);
	}
	
}
