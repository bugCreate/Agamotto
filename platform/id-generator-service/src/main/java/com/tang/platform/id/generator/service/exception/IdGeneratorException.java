package com.tang.platform.id.generator.service.exception;

public class IdGeneratorException extends Exception {
    public IdGeneratorException() {
        super();
    }

    public IdGeneratorException(String message) {
        super(message);
    }

    public IdGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
