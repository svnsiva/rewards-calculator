package com.charter.rewards.calculator.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class CustomException extends Exception {
    private final String exceptionMessage;
    private final HttpStatus httpStatus;

}
