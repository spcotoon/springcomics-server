package com.springcomics.api.exception;

public class UserNotLogin extends SpringComicsException{

    private static String MESSAGE = "로그인이 필요합니다.";

    public UserNotLogin() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
