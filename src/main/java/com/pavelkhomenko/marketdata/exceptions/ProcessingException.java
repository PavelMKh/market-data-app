package com.pavelkhomenko.marketdata.exceptions;

public class ProcessingException extends RuntimeException{
    public ProcessingException(String message) {
        super(message);
    }
}
