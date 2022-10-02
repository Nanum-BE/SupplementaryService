package com.nanum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ImgNotFoundException extends RuntimeException {
    public ImgNotFoundException(String message) {
        super(message);
    }

}
