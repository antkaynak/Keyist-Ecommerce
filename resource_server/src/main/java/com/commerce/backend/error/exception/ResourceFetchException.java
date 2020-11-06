package com.commerce.backend.error.exception;

public class ResourceFetchException extends RuntimeException {

    private static final long serialVersionUID = 6877491277661123459L;

    public ResourceFetchException() {
        super();
    }

    public ResourceFetchException(String s) {
        super(s);
    }

    public ResourceFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceFetchException(Throwable cause) {
        super(cause);
    }

}
