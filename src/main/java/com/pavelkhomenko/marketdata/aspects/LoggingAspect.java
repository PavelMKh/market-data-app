package com.pavelkhomenko.marketdata.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Before("execution(* com.pavelkhomenko.marketdata.controllers.AlphaVantageController.getCandlesHistory(..))")
    public void logBeforeGetCandlesFromAlphaVantageAdvice(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request to receive candles from AlphaVantage: : ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[4], requestArgs[1], requestArgs[2], requestArgs[0]);
    }

    @Before("execution(* com.pavelkhomenko.marketdata.controllers.MoexController.getCandlesByTicker(..))")
    public void logBeforeGetCandlesFromMoex(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request to receive candles from MOEX: ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[3], requestArgs[1], requestArgs[2], requestArgs[0]);
    }
}
