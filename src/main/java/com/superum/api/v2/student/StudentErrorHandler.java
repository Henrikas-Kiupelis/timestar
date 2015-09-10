package com.superum.api.v2.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how student specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class StudentErrorHandler {

    @ExceptionHandler
    void handleInvalidStudentException(InvalidStudentException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid Account format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid student format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleStudentNotFoundException(StudentNotFoundException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid Account format, check your JSON;", e);
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified student; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnsafeStudentDeleteException(UnsafeStudentDeleteException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid Account format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Cannot delete the specified student because it is still being used; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(StudentErrorHandler.class);

}
