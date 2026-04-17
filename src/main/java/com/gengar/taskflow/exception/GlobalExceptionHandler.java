package com.gengar.taskflow.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Centralized exception mapping.
 * Avoids implicit Spring DefaultHandlerExceptionResolver responses, guaranteeing RFC-7807 structure.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation Failed");
        problemDetail.setType(URI.create("urn:gengar:taskflow:problem:validation"));
        problemDetail.setTitle("Invalid Request Content");
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errors", errors);
        
        return problemDetail;
    }

    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handleDataAccessException(DataAccessException ex) {
        // Spring Data translates MongoExceptions into DataAccessExceptions.
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Database operation failed");
        problemDetail.setType(URI.create("urn:gengar:taskflow:problem:database"));
        problemDetail.setTitle("Database Error");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setType(URI.create("urn:gengar:taskflow:problem:internal"));
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
