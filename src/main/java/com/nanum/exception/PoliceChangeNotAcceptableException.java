package com.nanum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PoliceChangeNotAcceptableException extends RuntimeException {
    public PoliceChangeNotAcceptableException(String format) {
        super(format);
    }
}
