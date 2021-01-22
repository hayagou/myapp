package com.hayagou.myapp.advice.exception;

public class CFileException extends RuntimeException{
    public CFileException() {
    }

    public CFileException(String message) {
        super(message);
    }

    public CFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
