package com.springcomics.api.exception;

public class ComicNotFound extends SpringComicsException {

    private static final String MESSAGE = "존재하지 않는 글입니다";

    public ComicNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
