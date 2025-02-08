package com.springcomics.api.exception;

public class AlreadyExistsNameException extends SpringComicsException{
    private final static String MESSAGE = "이미 사용중인 이름입니다.";

    public AlreadyExistsNameException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
