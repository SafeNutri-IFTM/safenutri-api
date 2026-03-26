package com.safenutri.safenutri_api.exception;

public class RestricaoNotFoundException extends RuntimeException{
    public RestricaoNotFoundException(String message) {
        super(message);
    }
}