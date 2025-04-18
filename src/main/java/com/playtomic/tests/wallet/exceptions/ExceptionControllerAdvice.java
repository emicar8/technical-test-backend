package com.playtomic.tests.wallet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(value = {ResourceNotFoundException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  protected ResponseEntity<ErrorMessage> handleResourceNotFoundException(RuntimeException ex) {
    String message = "resource not found";
    String description = ex.getMessage();
    return new ResponseEntity<>(new ErrorMessage(message, description), HttpStatus.NOT_FOUND);
  }
}
