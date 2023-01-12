package org.homeservice.util.exception;

public class ConnectionBrokenException extends RuntimeException {
    public ConnectionBrokenException() {
    }

    public ConnectionBrokenException(String message) {
        super(message);
    }

    public ConnectionBrokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionBrokenException(Throwable cause) {
        super(cause);
    }
}
