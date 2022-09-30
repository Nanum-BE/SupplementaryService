package com.nanum.exception;

public class PoliceNotFoundException extends RuntimeException {
    public PoliceNotFoundException(String format) {
        super(format);
    }
}
