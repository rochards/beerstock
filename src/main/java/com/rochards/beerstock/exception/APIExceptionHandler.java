package com.rochards.beerstock.exception;

import com.rochards.beerstock.exception.type.BeerAlreadyExistException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;

@ControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(BeerAlreadyExistException.class)
    public ResponseEntity<Object> handleBeerAlreadyExistException(BeerAlreadyExistException ex, WebRequest request) {

        var status = HttpStatus.BAD_REQUEST;
        String error = ex.getMessage();
        APIError apiError = new APIError(OffsetDateTime.now(), status.value(), ex.getLocalizedMessage());

        return new ResponseEntity<Object>(apiError, new HttpHeaders(), status);
    }
}
