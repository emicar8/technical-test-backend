package com.playtomic.tests.wallet.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<ErrorMessage> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    String message = "resource not found";
    String description = ex.getMessage();
    return new ResponseEntity<>(new ErrorMessage(message, description), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ResponseEntity<ErrorMessage> handleConstraintViolationException(
      ConstraintViolationException ex) {
    String message = ex.getMessage();
    StringBuilder stringBuilder = new StringBuilder();
    ex.getConstraintViolations()
        .forEach(
            (constraintViolation) -> {
              stringBuilder.append(" [");
              stringBuilder.append(constraintViolation.getPropertyPath().toString());
              stringBuilder.append("] ");
              stringBuilder.append(constraintViolation.getMessage());
            });

    return new ResponseEntity<>(
        new ErrorMessage(message, stringBuilder.toString()), HttpStatus.BAD_REQUEST);
  }
}
