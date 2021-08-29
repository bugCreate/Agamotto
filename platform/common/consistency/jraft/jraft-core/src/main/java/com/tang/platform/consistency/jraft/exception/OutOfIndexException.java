package com.tang.platform.consistency.jraft.exception;

public class OutOfIndexException extends JraftRuntimeException {
    public OutOfIndexException(String message) {
        super(message);
    }

    public OutOfIndexException(String message, Throwable cause) {
        super(message, cause);
    }
}
