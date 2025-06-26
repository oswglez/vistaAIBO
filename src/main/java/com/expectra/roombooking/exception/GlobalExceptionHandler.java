package com.expectra.roombooking.exception; // Or .advice

import com.expectra.roombooking.dto.ErrorResponseDTO;
import org.slf4j.Logger; // SLF4J Logger import
import org.slf4j.LoggerFactory; // SLF4J LoggerFactory import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
// Optional imports if you uncomment the validation handler
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatusCode;
// import java.util.Map;
// import java.util.HashMap;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Define the SLF4J Logger
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handler for AuthenticationException (401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        String requestPath = request.getDescription(false).replace("uri=", "");
        
        // Log the authentication failure
        log.warn("Authentication failed: {} on path {}", ex.getMessage(), requestPath);
        
        String details;
        switch (ex.getErrorCode()) {
            case "NO_ROLES_ASSIGNED":
                details = "Please contact your administrator to assign appropriate roles to your account.";
                break;
            case "ACCOUNT_DISABLED":
                details = "Your account has been deactivated. Please contact the administrator for assistance.";
                break;
            case "INVALID_CREDENTIALS":
            default:
                details = "Please verify your credentials and try again.";
                break;
        }
        
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
            ex.getErrorCode(),
            ex.getMessage(),
            details,
            requestPath
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Handler for ResourceNotFoundException (404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found", // Generic error type for 404
                ex.getMessage(), // Specific message from the exception
                request.getDescription(false).replace("uri=", "") // Request path
        );
        // Log the specific not found error (optional, could be DEBUG level)
        log.warn("Resource not found: {} on path {}", ex.getMessage(), errorResponse.getPath());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handler for IllegalArgumentException (400 Bad Request - useful for validation)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request", // Generic error type for 400
                ex.getMessage(), // Specific message from the exception (e.g., "Invalid Media Type provided...")
                request.getDescription(false).replace("uri=", "")
        );
        // Log the bad request error
        log.warn("Illegal argument/Bad request: {} on path {}", ex.getMessage(), errorResponse.getPath());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Generic Handler for any other unhandled exception (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        // Log the full stack trace for unexpected errors - CRITICAL for debugging
        String requestPath = request.getDescription(false).replace("uri=", "");
        log.error("Unexpected error occurred processing request to path [{}]: ", requestPath, ex); // Log exception details

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected internal error occurred. Please try again later.", // Generic message for the client
                requestPath
        );
        // DO NOT expose ex.getMessage() or ex details directly to the client in generic 500 errors
        // unless absolutely certain it contains no sensitive information.
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- Optional: Handler for Spring Validation Errors (@Valid) ---
    /*
    @Override // Overrides method from ResponseEntityExceptionHandler
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage()));

        // Log validation errors
        String requestPath = request.getDescription(false).replace("uri=", "");
        log.warn("Validation failed for request to path [{}]: {}", requestPath, errors);

         ErrorResponse errorResponse = new ErrorResponse(
                 LocalDateTime.now(),
                 HttpStatus.BAD_REQUEST.value(),
                 "Validation Error",
                 errors.toString(), // Or a friendlier message, maybe put errors in a separate field
                 requestPath
         );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    */
}