package com.commerce.backend.error.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1177491237661223459L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String s) {
        super(s);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

}
