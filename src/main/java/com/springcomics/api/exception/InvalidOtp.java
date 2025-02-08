package com.springcomics.api.exception;

public class InvalidOtp extends SpringComicsException {
  private static final String MESSAGE = "유효하지 않은 OTP 입니다.";

  public InvalidOtp() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
