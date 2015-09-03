package com.superum.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 * Handles how app-wide exceptions are translated into HTTP responses
 *
 * This is used for when the default exception response gives too much/too little info
 * </pre>
 */
@ControllerAdvice
public class GenericErrorHandler {

    @ExceptionHandler
    void handleDatabaseException(DatabaseException e, HttpServletResponse response) throws IOException {
        LOG.error("Database couldn't return value", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database couldn't return value; " + e.getMessage());
    }

    @ExceptionHandler
    void handleDataAccessException(DataAccessException e, HttpServletResponse response) throws Exception {
        LOG.error("Unexpected error occurred", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error occurred");
    }

    @ExceptionHandler
    void handleAssertionError(AssertionError e, HttpServletResponse response) throws Exception {
        LOG.error("Something has gone terribly wrong. REALLY TERRIBLY!", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something has gone terribly wrong. REALLY TERRIBLY!");
    }

    @ExceptionHandler
    void handleIllegalStateException(IllegalStateException e, HttpServletResponse response) throws Exception {
        LOG.error("Something wrong happened in the server", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something wrong happened in the server");
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws Exception {
        LOG.error("Someone put something where they shouldn't", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Someone put something where they shouldn't; " + e.getMessage());
    }

    @ExceptionHandler
    void handleNullPointerException(NullPointerException e, HttpServletResponse response) throws Exception {
        LOG.error("Someone somewhere a null pointer", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Someone somewhere a null pointer");
    }

    // PRIVATE

    private static final Logger LOG = LoggerFactory.getLogger(GenericErrorHandler.class);

}
