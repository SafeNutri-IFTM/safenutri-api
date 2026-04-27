package com.safenutri.safenutri_api.exception;

public class ReceitaNotFoundException extends RuntimeException{
    public ReceitaNotFoundException(String message) {
        super(message);
    }
}
