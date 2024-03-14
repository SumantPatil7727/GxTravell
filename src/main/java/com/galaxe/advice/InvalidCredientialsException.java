package com.galaxe.advice;

public class InvalidCredientialsException extends Exception {
  public InvalidCredientialsException() {
    super();
  }

  public InvalidCredientialsException(String msg) {
    super(msg);
  }
}
