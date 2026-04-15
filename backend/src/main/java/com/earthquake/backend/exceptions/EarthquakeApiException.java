package com.earthquake.backend.exceptions;

public class EarthquakeApiException extends RuntimeException {
    public EarthquakeApiException(String message, Throwable cause) {
        super(message, cause);
    }
}