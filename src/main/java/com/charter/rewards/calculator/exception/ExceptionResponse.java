package com.charter.rewards.calculator.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ExceptionResponse {
    private String exceptionMessage;
    private HttpStatus httpStatus;
}
