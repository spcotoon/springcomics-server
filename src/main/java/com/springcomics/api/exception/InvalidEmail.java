package com.springcomics.api.exception;

public class InvalidEmail extends SpringComicsException {
    private static final String MESSAGE = "유효하지 않은 이메일 입니다.";

    public InvalidEmail() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 422 ;
    }
}
