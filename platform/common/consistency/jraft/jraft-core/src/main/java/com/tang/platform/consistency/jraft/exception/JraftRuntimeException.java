package com.tang.platform.consistency.jraft.exception;

public class JraftRuntimeException extends RuntimeException {
    public JraftRuntimeException(String message) {
        super(message);
    }

    public JraftRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
