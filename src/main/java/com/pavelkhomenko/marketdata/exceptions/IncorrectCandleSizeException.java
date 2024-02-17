package com.pavelkhomenko.marketdata.exceptions;

public class IncorrectCandleSizeException extends RuntimeException {
    public IncorrectCandleSizeException(String message) {
        super(message);
    }
}
