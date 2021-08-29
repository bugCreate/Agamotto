package com.tang.platform.consistency.jraft.exception;

public class JraftException extends Exception {
    public JraftException(String message) {
        super(message);
    }

    public JraftException(String message, Throwable cause) {
        super(message, cause);
    }
}
