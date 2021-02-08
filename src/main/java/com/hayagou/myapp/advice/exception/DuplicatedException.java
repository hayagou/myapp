package com.hayagou.myapp.advice.exception;

public class DuplicatedException extends RuntimeException{
    public DuplicatedException() {
    }

    public DuplicatedException(String message) {
        super(message);
    }

    public DuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
