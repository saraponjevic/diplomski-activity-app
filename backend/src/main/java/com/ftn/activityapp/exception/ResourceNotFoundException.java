package com.ftn.activityapp.exception;

// kad nesto ne postoji, npr korisnik

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
