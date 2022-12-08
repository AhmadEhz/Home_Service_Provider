package org.homeservice.util.exception;

public class NotVerifiedException extends RuntimeException {
    public NotVerifiedException() {
    }

    public NotVerifiedException(String message) {
        super(message);
    }

    public NotVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotVerifiedException(Throwable cause) {
        super(cause);
    }
}
