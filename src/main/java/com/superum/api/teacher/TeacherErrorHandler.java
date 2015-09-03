package com.superum.api.teacher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles how Teacher specific exceptions are translated into HTTP responses
 */
@ControllerAdvice
public class TeacherErrorHandler {

    @ExceptionHandler
    void handleInvalidCustomerException(InvalidTeacherException e, HttpServletResponse response) throws IOException {
        LOG.error("Invalid Teacher format, check your JSON;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Invalid Teacher format, check your JSON; " + e.getMessage());
    }

    @ExceptionHandler
    void handleCustomerNotFoundException(TeacherNotFoundException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot find the specified Teacher;", e);
        response.sendError(HttpStatus.NOT_FOUND.value(), "Cannot find the specified Teacher; " + e.getMessage());
    }

    @ExceptionHandler
    void handleUnsafeCustomerDeleteException(UnsafeTeacherDeleteException e, HttpServletResponse response) throws IOException {
        LOG.error("Cannot delete the specified Teacher because it is still being used;", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Cannot delete the specified Teacher because it is still being used; " + e.getMessage());
    }

    @ExceptionHandler(DuplicateTeacherException.class)
    void handleDuplicateTeacherException(DuplicateTeacherException e, HttpServletResponse response) throws IOException {
        LOG.error("A teacher with specified email already exists;", e);
        response.sendError(HttpStatus.CONFLICT.value(), "A teacher with specified email already exists; " + e.getMessage());
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(TeacherErrorHandler.class);

}
