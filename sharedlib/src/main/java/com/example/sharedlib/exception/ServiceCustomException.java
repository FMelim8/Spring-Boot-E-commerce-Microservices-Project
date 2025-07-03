package com.example.sharedlib.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "localizedMessage"})
@JsonPropertyOrder({"timestamp", "status", "error", "message", "path"})
public class ServiceCustomException extends RuntimeException {

    private String error;
    private int status;
    private Instant timestamp;
    private String message;
    private String path;

    public ServiceCustomException(String error, String errorMessage, int status, String path) {
        this.timestamp = Instant.now();
        this.status = status;
        this.error = error;
        this.message = errorMessage;
        this.path = path;
    }
}
