package com.springcomics.api.exception;

public class InvalidJwtToken extends SpringComicsException {

    private static final String MESSAGE = "유효하지 않은 아이디 입니다.";

    public InvalidJwtToken() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
