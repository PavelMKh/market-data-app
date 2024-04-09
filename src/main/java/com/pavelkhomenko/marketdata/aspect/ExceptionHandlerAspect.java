package com.pavelkhomenko.marketdata.aspect;

import com.pavelkhomenko.marketdata.exceptions.ProcessingException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectCandleSizeException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectDateException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectTickerNameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAspect {
    @ExceptionHandler({IncorrectTickerNameException.class,
            IncorrectDateException.class,
            IncorrectCandleSizeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectDateCandleSizeTicker(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

}
