package com.pavelkhomenko.marketdata.service;

import com.pavelkhomenko.marketdata.Constants;
import com.pavelkhomenko.marketdata.mapping.candles.AlphaVantageCandleMapping;
import com.pavelkhomenko.marketdata.mapping.candles.MoexCandleMapping;
import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.exceptions.IncorrectCandleSizeException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectDateException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectTickerNameException;
import com.pavelkhomenko.marketdata.repository.CandleRepository;
import com.pavelkhomenko.marketdata.util.CsvFileGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleHistoryService {
    private final MoexCandleMapping requestMoexProcessor;
    private final AlphaVantageCandleMapping requestAlphaVantageProcessor;
    private final CandleRepository candleRepository;
    private final CsvFileGenerator csvFileGenerator;

    public List<Candle> getAlphaVantageCandles(String ticker, int interval, String apikey,
                                              LocalDate start, LocalDate end) {
        log.info("Candle history from AlphaVantage requested: ticker - " + ticker +
                " interval - " + interval + " start date - " + start +
                " end date - " + end);
        if (LocalDate.now().isBefore(start) || LocalDate.now().isBefore(end)) {
            throw new IncorrectDateException("Future dates cannot be query parameters");
        }
        if (!Constants.ALPHA_VANTAGE_CANDLE_SIZE.contains(interval)) {
            throw new IncorrectCandleSizeException("The candle size is not valid. Valid values: " +
                    "1 (1 minute), 5 (5 minutes), 15 (1 minutes), 30 (30 minutes), 60 (60 minutes)");
        }
        if (ticker.isEmpty() || ticker.isBlank()) {
            throw new IncorrectTickerNameException("Ticker can't be empty or blank");
        }
        List<Candle> candles = requestAlphaVantageProcessor.getCandleSet(ticker, interval, apikey, start, end);
        candleRepository.saveAll(candles);
        return candles;
    }

    public List<Candle> getMoexCandles(String ticker, int interval, LocalDate start,
                                      LocalDate end) {
        log.info("Candle history from MOEX requested: ticker - " + ticker +
                " interval - " + interval + " start date - " + start +
                " end date - " + end);
        if (LocalDate.now().isBefore(start) || LocalDate.now().isBefore(end)) {
            throw new IncorrectDateException("Future dates cannot be query parameters");
        }

        if (start.isAfter(end)) {
            throw new IncorrectDateException("The start date cannot be later than the end date");
        }

        if (!Constants.MOEX_CANDLE_SIZE.contains(interval)) {
            throw new IncorrectCandleSizeException("The candle size is not valid. Valid values: " +
                    "1 (1 minute), 10 (10 minutes), 60 (1 hour), " +
                    "24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)");
        }
        if (ticker.isEmpty() || ticker.isBlank()) {
            throw new IncorrectTickerNameException("Ticker can't be empty or blank");
        }
        return requestMoexProcessor.getCandleSet(ticker, interval, start, end);
    }

    public List<Candle> getCandlesFromDatabase(String ticker, int interval, String start,
                                               String end) {
        log.info("Candle history from Database requested: ticker - " + ticker +
                " interval - " + interval + " start date - " + start +
                " end date - " + end);
        return candleRepository.getCandlesBetweenDates(convertStringToOffsetDateTime(start),
                convertStringToOffsetDateTime(end), ticker, interval);
    }

    public List<Candle> reloadRepositoryMoex(String defaultStartDate)  {
        log.info("Reloading MOEX shares repo requested");
        Set<String> moexTickers = candleRepository.findDistinctByTickerMoex();
        List<Candle> reloadedCandles = new CopyOnWriteArrayList<>();
        LocalDate currentDate = LocalDate.now();
        moexTickers.parallelStream().forEach(ticker -> Constants.MOEX_CANDLE_SIZE.parallelStream().forEach(interval -> {
            LocalDate lastUpdatedDate = getLastUpdatedDate(ticker, interval, defaultStartDate);
            log.info("Getting data from MOEX: ticker {}, candle size {}, start date {}, end date {} ",
                    ticker, interval, lastUpdatedDate, currentDate);
            reloadedCandles.addAll(getMoexCandles(ticker, interval, lastUpdatedDate, currentDate));
        }));
        candleRepository.saveAll(reloadedCandles);
        return reloadedCandles;
    }

    /* This method allows you to get the latest date of a candle
    stored in the database for a ticker and interval*/
    private LocalDate getLastUpdatedDate(String ticker, int interval, String defaultStartDate) {
        LocalDate lastUpdatedDate = candleRepository.getLastDateForTicker(ticker, interval);
        if (lastUpdatedDate == null) {
            return LocalDate.parse(defaultStartDate);
        }
        return lastUpdatedDate;
    }

    public ByteArrayInputStream loadFromMoexToCsv(String ticker,
                                                   int interval,
                                                   LocalDate start,
                                                   LocalDate end) {
        log.info("MOEX candle history CSV file requested: ticker - " + ticker +
                " interval - " + interval + " start date - " + start +
                " end date - " + end);
        List<Candle> candles = getMoexCandles(ticker, interval, start, end);
        return csvFileGenerator.writeCandlesToCsv(candles);
    }

    public ByteArrayInputStream loadFromAlphaVantageToCsv(String ticker,
                                                          int interval,
                                                          String apikey,
                                                          LocalDate start,
                                                          LocalDate end) {
        log.info("AlphaVantage candle history CSV file requested: ticker - " + ticker +
                " interval - " + interval + " start date - " + start +
                " end date - " + end);
        List<Candle> candles = getAlphaVantageCandles(ticker, interval, apikey, start, end);
        return csvFileGenerator.writeCandlesToCsv(candles);
    }

    public ByteArrayInputStream loadFromRepoToCsv(String ticker,
                                                  int interval,
                                                  String start,
                                                  String end) {
        log.info("Repo candle history CSV file requested: ticker - " + ticker +
                " interval - " + interval + " start date - " + start +
                " end date - " + end);
        List<Candle> candles = getCandlesFromDatabase(ticker, interval, start, end);
        return csvFileGenerator.writeCandlesToCsv(candles);
    }

    private OffsetDateTime convertStringToOffsetDateTime(String date) {
        return OffsetDateTime.parse(date + "T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
