package com.softknife.release.exception;

import org.springframework.http.HttpStatus;

public class RecordNotFound extends RuntimeException {

    private static final Long serialVersionUID = 1L;

    public RecordNotFound(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}