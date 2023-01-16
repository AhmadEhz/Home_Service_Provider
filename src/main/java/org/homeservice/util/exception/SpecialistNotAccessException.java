package org.homeservice.util.exception;

public class SpecialistNotAccessException extends RuntimeException {
    public SpecialistNotAccessException() {
    }

    public SpecialistNotAccessException(String message) {
        super(message);
    }

    public SpecialistNotAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecialistNotAccessException(Throwable cause) {
        super(cause);
    }
}
