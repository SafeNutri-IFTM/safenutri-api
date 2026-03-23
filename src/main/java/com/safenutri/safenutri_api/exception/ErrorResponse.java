package com.safenutri.safenutri_api.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private int status;
    private String timestamp;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now().toString();
    }
}
