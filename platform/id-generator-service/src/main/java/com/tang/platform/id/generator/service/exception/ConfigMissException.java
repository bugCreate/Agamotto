package com.tang.platform.id.generator.service.exception;

public class ConfigMissException extends IdGeneratorException {
    public ConfigMissException() {
        super();
    }

    public ConfigMissException(String message) {
        super(message);
    }

    public ConfigMissException(String message, Throwable cause) {
        super(message, cause);
    }
}
