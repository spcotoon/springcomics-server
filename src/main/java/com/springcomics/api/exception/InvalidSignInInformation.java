package com.springcomics.api.exception;

public class InvalidSignInInformation extends SpringComicsException {

    private static final String MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";

    public InvalidSignInInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
