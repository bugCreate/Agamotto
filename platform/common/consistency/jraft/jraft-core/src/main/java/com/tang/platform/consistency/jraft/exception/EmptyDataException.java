package com.tang.platform.consistency.jraft.exception;

public class EmptyDataException extends JraftRuntimeException {
    public EmptyDataException(String message) {
        super(message);
    }

    public EmptyDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
