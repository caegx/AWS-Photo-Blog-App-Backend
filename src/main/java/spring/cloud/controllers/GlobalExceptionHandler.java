package spring.cloud.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import spring.cloud.exceptions.InvalidOperationException;
import spring.cloud.exceptions.ResourceNotFoundException;
import spring.cloud.exceptions.dtos.ExceptionResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getAllErrors().forEach(error -> errors.put(error.getDefaultMessage(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidOperationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(ResourceNotFoundException exception) {
        return ResponseEntity.status(exception.getStatus()).body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ExceptionResponse> handleException(IOException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<ExceptionResponse> handleException(SdkClientException exception) {
        var message = exception.getMessage().split("\\. \\(")[0];

        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }

    @ExceptionHandler(AwsServiceException.class)
    public ResponseEntity<ExceptionResponse> handleException(AwsServiceException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage()));
    }
}
