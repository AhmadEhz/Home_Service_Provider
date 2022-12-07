package org.homeservice.util.exception;

public class CustomIllegalArgumentException extends java.lang.IllegalArgumentException {
    //todo:change classname
    public CustomIllegalArgumentException() {
    }

    public CustomIllegalArgumentException(String s) {
        super(s);
    }

    public CustomIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
