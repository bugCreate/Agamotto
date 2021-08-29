package com.tang.platform.consistency.jraft.exception;

public class JraftLogException extends JraftRuntimeException {
    public JraftLogException(String message) {
        super(message);
    }

    public JraftLogException(String message, Throwable cause) {
        super(message, cause);
    }
}
