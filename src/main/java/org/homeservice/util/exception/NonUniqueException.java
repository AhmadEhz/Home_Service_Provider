package org.homeservice.util.exception;

public class NonUniqueException extends RuntimeException{
    public NonUniqueException() {
    }

    public NonUniqueException(String message) {
        super(message);
    }

    public NonUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonUniqueException(Throwable cause) {
        super(cause);
    }
}
