package com.galaxe.advice;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlee {

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ErrorDetails> handleDuplicateEmailException(DuplicateEmailException ex) {
    ErrorDetails details =
        new ErrorDetails(1001, LocalDateTime.now(), ex.getMessage(), "404-DuplicateEmailException");
    return new ResponseEntity<ErrorDetails>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidCredientialsException.class)
  public ResponseEntity<ErrorDetails> handleInvalidCredientialsException(
      InvalidCredientialsException ex) {
    ErrorDetails details =
        new ErrorDetails(
            1003, LocalDateTime.now(), ex.getMessage(), "404-InvalidCredientialsException");
    return new ResponseEntity<ErrorDetails>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex) {
    ErrorDetails details =
        new ErrorDetails(
            1004, LocalDateTime.now(), ex.getMessage(), "404-InvalidUserNotFoundException");
    return new ResponseEntity<ErrorDetails>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ErrorDetails> handleInvalidPasswordException(InvalidPasswordException ex) {
    ErrorDetails details =
        new ErrorDetails(
            1002, LocalDateTime.now(), ex.getMessage(), "404-InvalidPasswordException");
    return new ResponseEntity<ErrorDetails>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(LoginFailedException.class)
  public ResponseEntity<ErrorDetails> handleLoginFailedException(LoginFailedException ex) {
    ErrorDetails details =
        new ErrorDetails(1005, LocalDateTime.now(), ex.getMessage(), "404-LoginFailedException");
    return new ResponseEntity<ErrorDetails>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(SubscriptionFailedException.class)
  public ResponseEntity<ErrorDetails> handleSubscriptionFailedException(
      SubscriptionFailedException ex) {
    ErrorDetails details =
        new ErrorDetails(
            1006, LocalDateTime.now(), ex.getMessage(), "404-SubscriptionFailedException");
    return new ResponseEntity<ErrorDetails>(details, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
