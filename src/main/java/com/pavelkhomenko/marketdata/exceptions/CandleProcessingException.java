package com.pavelkhomenko.marketdata.exceptions;

public class CandleProcessingException extends RuntimeException{
    public CandleProcessingException(String message) {
        super(message);
    }
}
