package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.exceptions.IncorrectCandleSizeException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectDateException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectTickerNameException;
import com.pavelkhomenko.marketdata.repository.CandleMongoRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandleHistoryService {
    @NotNull
    private final MoexCandleProcessor requestMoexProcessor;
    @NotNull
    private final AlphaVantageCandleProcessor requestAlphaVantageProcessor;
    @NotNull
    private final CandleMongoRepository candleMongoRepository;

    public List<Candle> getAlphaVantageCandles(String ticker, int interval, String apikey,
                                              LocalDate start, LocalDate end) throws JsonProcessingException {
        if (LocalDate.now().isBefore(start) || LocalDate.now().isBefore(end)) {
            throw new IncorrectDateException("Future dates cannot be query parameters");
        }
        if (interval != 1 && interval != 10 && interval != 60 && interval != 24 && interval != 7 && interval != 31 &&
                interval != 4) {
            throw new IncorrectCandleSizeException("The candle size is not valid. Valid values: " +
                    "1 (1 minute), 5 (5 minutes), 15 (1 minutes), 30 (30 minutes), 60 (60 minutes)");
        }
        if (ticker.isEmpty() || ticker.isBlank()) {
            throw new IncorrectTickerNameException("Ticker can't be empty or blank");
        }
        List<Candle> candles = requestAlphaVantageProcessor.getCandleSet(ticker, interval, apikey, start, end);
        candleMongoRepository.saveAll(candles);
        return candles;
    }

    public List<Candle> getMoexCandles(String ticker, int interval, LocalDate start,
                                      LocalDate end) throws JsonProcessingException {
        if (LocalDate.now().isBefore(start) || LocalDate.now().isBefore(end)) {
            throw new IncorrectDateException("Future dates cannot be query parameters");
        }
        if (interval != 1 && interval != 10 && interval != 60 && interval != 24 && interval != 7 && interval != 31 &&
                interval != 4) {
            throw new IncorrectCandleSizeException("The candle size is not valid. Valid values: " +
                    "1 (1 minute), 10 (10 minutes), 60 (1 hour), " +
                    "24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)");
        }
        if (ticker.isEmpty() || ticker.isBlank()) {
            throw new IncorrectTickerNameException("Ticker can't be empty or blank");
        }
        List<Candle> candles = requestMoexProcessor.getCandleSet(ticker, interval, start, end);
        candleMongoRepository.saveAll(candles);
        return candles;
    }

    public List<Candle> getCandlesFromDatabase(String ticker, int interval, LocalDate start,
                                              LocalDate end) {
        Instant startInstant = start.atStartOfDay().atZone(ZoneOffset.UTC).toInstant();
        Instant endInstant = end.atStartOfDay().atZone(ZoneOffset.UTC).toInstant();
        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);
        Sort sort = Sort.by(Sort.Direction.DESC, "startDateTime");
        return candleMongoRepository.getCandlesBetweenDates(startDate, endDate, ticker, interval, sort);
    }


}
