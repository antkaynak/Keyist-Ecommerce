package com.commerce.backend.error.exception;


public class InvalidArgumentException extends RuntimeException {

    private static final long serialVersionUID = -1262173968380116559L;

    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String s) {
        super(s);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }

}

