package com.pavelkhomenko.marketdata.exceptions;

public class IncorrectTickerNameException extends RuntimeException {
    public IncorrectTickerNameException(String message) {
        super(message);
    }
}
