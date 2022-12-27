package org.homeservice.util.exception;

public class InsufficientAmountException extends RuntimeException {
    public InsufficientAmountException() {
    }

    public InsufficientAmountException(String message) {
        super(message);
    }

    public InsufficientAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientAmountException(Throwable cause) {
        super(cause);
    }
}
