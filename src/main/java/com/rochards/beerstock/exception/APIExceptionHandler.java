package com.rochards.beerstock.exception;

import com.rochards.beerstock.exception.type.BeerAlreadyExistException;
import com.rochards.beerstock.exception.type.BeerNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BeerAlreadyExistException.class)
    public ResponseEntity<Object> handleBeerAlreadyExistException(BeerAlreadyExistException ex, WebRequest request) {

        var status = HttpStatus.BAD_REQUEST;
        String error = ex.getMessage();
        APIError apiError = new APIError(OffsetDateTime.now(), status.value(), error);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), status);
    }

    @ExceptionHandler(BeerNotFoundException.class)
    public ResponseEntity<Object> handleBeerNotFoundException(BeerNotFoundException ex, WebRequest request) {

        var status = HttpStatus.BAD_REQUEST;
        String error = ex.getMessage();
        APIError apiError = new APIError(OffsetDateTime.now(), status.value(), error);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        var message = "One or more fields are invalid!";
        APIError apiError = new APIError(OffsetDateTime.now(), status.value(), message, errors);

        return super.handleExceptionInternal(ex, apiError, headers, status, request);
    }
}
