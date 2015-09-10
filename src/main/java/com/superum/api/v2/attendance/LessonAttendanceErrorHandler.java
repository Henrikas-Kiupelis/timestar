package com.superum.api.v2.attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how lesson attendance specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class LessonAttendanceErrorHandler {

    @ExceptionHandler
    void handleInvalidLessonAttendanceException(InvalidLessonAttendanceException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid lesson attendance format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid lesson attendance format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleLessonAttendanceNotFoundException(LessonAttendanceNotFoundException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot find the specified lesson attendance;", e);
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified lesson attendance; " + e.getMessage());
    }

    @ExceptionHandler
    void handleDuplicateLessonAttendanceException(DuplicateLessonAttendanceException e, HttpServletResponse response) throws IOException {
        LOG.error("Specified lesson attendance already exists;", e);
        response.sendError(HttpStatus.CONFLICT.value(), "Specified lesson attendance already exists; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(LessonAttendanceErrorHandler.class);

}
