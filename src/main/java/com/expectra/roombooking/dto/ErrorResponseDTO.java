package com.expectra.roombooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {
    
    private String error;
    private String message;
    private String details;
    private LocalDateTime timestamp;
    private String path;
    
    public ErrorResponseDTO(String error, String message, String details, String path) {
        this.error = error;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }
    
    public static ErrorResponseDTO of(String error, String message, String details, String path) {
        return new ErrorResponseDTO(error, message, details, path);
    }
} 