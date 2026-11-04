package com.example.studentmanagement.exception;

public class TooManyLoginAttemptsException extends RuntimeException {

    private final long retryAfterSeconds;
    public TooManyLoginAttemptsException(long retryAfterSeconds) {

        super("Too many failed login attempts. Try again later.");
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds(){
        return retryAfterSeconds;
    }
}
