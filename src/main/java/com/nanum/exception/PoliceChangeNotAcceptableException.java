package com.nanum.exception;

public class PoliceChangeNotAcceptableException extends RuntimeException {
    public PoliceChangeNotAcceptableException(String format) {
        super(format);
    }
}
