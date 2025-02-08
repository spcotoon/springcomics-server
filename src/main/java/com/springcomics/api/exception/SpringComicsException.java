package com.springcomics.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SpringComicsException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public SpringComicsException(String message) {
        super(message);
    }

    public SpringComicsException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }
}
