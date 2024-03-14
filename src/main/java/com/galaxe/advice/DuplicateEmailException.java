package com.galaxe.advice;

public class DuplicateEmailException extends Exception {
  public DuplicateEmailException() {
    super();
  }

  public DuplicateEmailException(String msg) {
    super(msg);
  }
}
