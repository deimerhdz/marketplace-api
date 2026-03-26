package com.taurupro.marketplace.domain.exception;

public class RequestStillProcessingException extends RuntimeException {
    public RequestStillProcessingException(String message) {
        super(message);
    }
}
