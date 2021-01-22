package com.hayagou.myapp.advice.exception;

public class CNotCreateDirException extends RuntimeException {
    public CNotCreateDirException() {
        super();
    }

    public CNotCreateDirException(String message) {
        super(message);
    }

    public CNotCreateDirException(String message, Throwable cause) {
        super(message, cause);
    }
}
