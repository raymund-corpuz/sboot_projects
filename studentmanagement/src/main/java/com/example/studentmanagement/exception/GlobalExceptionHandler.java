package com.example.studentmanagement.exception;

import com.example.studentmanagement.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex){
      return build(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex){
      return build(HttpStatus.CONFLICT, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
      Map<String, String> fieldErrors = new LinkedHashMap<>();
      ex.getBindingResult().getFieldErrors()
              .forEach(error ->fieldErrors.putIfAbsent(error.getField(),error.getDefaultMessage()));

      ErrorResponse body = ErrorResponse.ofValidation(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_GATEWAY.getReasonPhrase(), "Validation failed", fieldErrors);
      return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex){
      return build(HttpStatus.BAD_REQUEST, "Malformed JSON or invalid value in request body");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
      return build(HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + ex.getMessage()+"'");
  }

  //Safety net for the race condition mentioned in step3
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex){
      return build(HttpStatus.CONFLICT, "The request conflicts with existing data");
    }

    //Last resort: never leak internal details to the client
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex){
      return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

  private ResponseEntity<ErrorResponse> build(HttpStatus status, String message){
      ErrorResponse body = ErrorResponse.of(status.value(), status.getReasonPhrase(),message);

      return ResponseEntity.status(status).body(body);
  }
}
