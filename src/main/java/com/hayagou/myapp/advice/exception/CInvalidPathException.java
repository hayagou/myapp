package com.hayagou.myapp.advice.exception;

public class CInvalidPathException extends RuntimeException {
    public CInvalidPathException() {
    }

    public CInvalidPathException(String message) {
        super(message);
    }

    public CInvalidPathException(String message, Throwable cause) {
        super(message, cause);
    }
}
