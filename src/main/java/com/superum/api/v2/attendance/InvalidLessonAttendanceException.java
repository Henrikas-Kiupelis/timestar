package com.superum.api.v2.attendance;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of lesson attendance
 * does not match any of the use cases
 * </pre>
 */
public class InvalidLessonAttendanceException extends DatabaseException {

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
