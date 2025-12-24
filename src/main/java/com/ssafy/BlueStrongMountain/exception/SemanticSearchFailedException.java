package com.ssafy.BlueStrongMountain.exception;

public class SemanticSearchFailedException extends RuntimeException {

    public SemanticSearchFailedException(String message) {
        super(message);
    }

    public SemanticSearchFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
