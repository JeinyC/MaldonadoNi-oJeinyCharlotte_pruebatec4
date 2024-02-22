package com.example.Agency.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(String message) {
        super(message);
    }
}
