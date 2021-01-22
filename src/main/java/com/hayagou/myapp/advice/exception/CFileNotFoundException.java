package com.hayagou.myapp.advice.exception;

public class CFileNotFoundException extends RuntimeException {
    public CFileNotFoundException() {
        super();
    }

    public CFileNotFoundException(String message) {
        super(message);
    }

    public CFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
