package com.nanum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PoliceNotFoundException extends RuntimeException {
    public PoliceNotFoundException(String format) {
        super(format);
    }
}
