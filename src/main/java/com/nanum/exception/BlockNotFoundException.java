package com.nanum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BlockNotFoundException extends RuntimeException {
    public BlockNotFoundException(String format) {
            super(format);
    }
}
