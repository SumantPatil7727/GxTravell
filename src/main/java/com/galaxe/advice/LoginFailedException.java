package com.galaxe.advice;

public class LoginFailedException extends Exception {

  public LoginFailedException() {
    super();
  }

  public LoginFailedException(String msg) {
    super(msg);
  }
}
