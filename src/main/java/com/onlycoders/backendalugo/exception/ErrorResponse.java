package com.onlycoders.backendalugo.exception;

public class ErrorResponse {
    private String message;

    public ErrorResponse(String codigo) {
        this.message = codigo;

    }

    public String getMessage() {
        return message;
    }
}
