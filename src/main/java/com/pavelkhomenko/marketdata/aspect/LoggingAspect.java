package com.pavelkhomenko.marketdata.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Before("execution(* getCandlesHistoryAlphaVantage(..))")
    public void logBeforeGetCandlesFromAlphaVantageAdvice(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request candles from AlphaVantage: : ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[4], requestArgs[1], requestArgs[2], requestArgs[0]);
    }

    @Before("execution(* getCandlesHistoryMoex(..))")
    public void logBeforeGetCandlesFromMoex(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request candles from MOEX: ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[3], requestArgs[1], requestArgs[2], requestArgs[0]);
    }

    @Before("execution(* getCandlesHistoryFromRepository(..))")
    public void logCandlesHistoryFromRepository(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request candles from repo: ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[3], requestArgs[1], requestArgs[2], requestArgs[0]);
    }

    @Before("execution(* com.pavelkhomenko.marketdata.controller.CandlesHistoryController.reloadRepositoryMoex(..))")
    public void logUploadingCandlesIntoDatabase() {
        log.info("uploading candles into database has started");
    }

    @Before("execution(* getCandlesHistoryFromRepositoryCsv(..))")
    public void logCandlesHistoryFromRepositoryToCsv(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request candles from repo to csv: ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[3], requestArgs[1], requestArgs[2], requestArgs[0]);
    }

    @Before("execution(* getCandlesHistoryAlphaVantageCsv(..))")
    public void logCandlesHistoryFromAlphaVantageToCsv(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request candles from AlphaVantage to csv: ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[4], requestArgs[1], requestArgs[2], requestArgs[0]);
    }

    @Before("execution(* getCandlesHistoryMoexCsv(..))")
    public void logCandlesHistoryFromMoexToCsv(JoinPoint joinPoint) {
        Object[] requestArgs = joinPoint.getArgs();
        log.info("request candles from MOEX to csv: ticker {}, startSate {}, endDate {}, candleSize {}",
                requestArgs[3], requestArgs[1], requestArgs[2], requestArgs[0]);
    }
}
