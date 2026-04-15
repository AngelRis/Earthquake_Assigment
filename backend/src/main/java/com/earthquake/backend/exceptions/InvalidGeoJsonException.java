package com.earthquake.backend.exceptions;

public class InvalidGeoJsonException extends RuntimeException {
    public InvalidGeoJsonException(String message) {
        super(message);
    }
}