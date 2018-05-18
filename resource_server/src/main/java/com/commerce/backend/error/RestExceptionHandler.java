package com.commerce.backend.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleError404(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, "Requested page does not exist", 404));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIO(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred in IO streams.", 500));
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity databaseError(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred in database connection.", 500));
    }

    //Handling access denied with OAuth2 in resource config
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity handleAccessDenied(Exception e)   {
//        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST,"Access denied.",400));
//    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolation(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Constraint violation", 400));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), 400));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, e.getMessage(), 409));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError(Exception e) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getClass().getName() + " " + e.getMessage(), 500));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}