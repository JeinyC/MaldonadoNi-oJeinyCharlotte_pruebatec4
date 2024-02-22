package com.example.Agency.exception;

public class MissingParametersException extends RuntimeException {
    public MissingParametersException(String message) {
        super(message);
    }
}
