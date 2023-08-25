package com.charter.rewards.calculator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= {CustomException.class})
    public ResponseEntity<ExceptionResponse> handleApiException (CustomException ex) {
        ExceptionResponse res = new ExceptionResponse();
        res.setExceptionMessage(ex.getExceptionMessage());
        res.setHttpStatus(ex.getHttpStatus());
        return new ResponseEntity<>(res, ex.getHttpStatus());
    }
}
