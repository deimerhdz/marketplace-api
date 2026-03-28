package com.taurupro.marketplace.domain.exception;

public class StrawAlreadyExistsException extends RuntimeException {
    public StrawAlreadyExistsException(String message) {
        super(message);
    }
}
