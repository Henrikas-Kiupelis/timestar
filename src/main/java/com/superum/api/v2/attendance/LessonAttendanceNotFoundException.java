package com.superum.api.v2.attendance;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when lesson attendance cannot be found
 */
public class LessonAttendanceNotFoundException extends DatabaseException {

	public LessonAttendanceNotFoundException() {
		super();
	}

	public LessonAttendanceNotFoundException(String message) {
		super(message);
	}

	public LessonAttendanceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LessonAttendanceNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
