package com.ftn.activityapp.exception;


// kad je zahtev los, npr email vec postoji
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
