package org.homeservice.controller.exception_handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.homeservice.util.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomIllegalArgumentException.class, NotFoundException.class, NonUniqueException.class,
            InsufficientAmountException.class})
    public String illegalArgument(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotVerifiedException.class)
    public String notVerified(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String notValid(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation c : ex.getConstraintViolations()) {
            message.append(c.getMessage()).append("\n");
        }
        return message.toString();
    }

}
