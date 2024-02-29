package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkhomenko.marketdata.Constants;
import com.pavelkhomenko.marketdata.candleprocessing.AlphaVantageCandleProcessor;
import com.pavelkhomenko.marketdata.candleprocessing.MoexCandleProcessor;
import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.exceptions.IncorrectCandleSizeException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectDateException;
import com.pavelkhomenko.marketdata.exceptions.IncorrectTickerNameException;
import com.pavelkhomenko.marketdata.repository.CandleMongoRepository;
import com.pavelkhomenko.marketdata.util.CsvFileGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandleHistoryService {
    @NotNull
    private final MoexCandleProcessor requestMoexProcessor;
    @NotNull
    private final AlphaVantageCandleProcessor requestAlphaVantageProcessor;
    @NotNull
    private final CandleMongoRepository candleMongoRepository;
    @NotNull
    private final CsvFileGenerator csvFileGenerator;
    ObjectMapper objectMapper = new ObjectMapper();

    public List<Candle> getAlphaVantageCandles(String ticker, int interval, String apikey,
                                              LocalDate start, LocalDate end) throws JsonProcessingException {
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
        candleMongoRepository.saveAll(candles);
        return candles;
    }

    public List<Candle> getMoexCandles(String ticker, int interval, LocalDate start,
                                      LocalDate end) throws JsonProcessingException {
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

    public List<Candle> getCandlesFromDatabase(String ticker, int interval, LocalDate start,
                                              LocalDate end) {
        Instant startInstant = start.atStartOfDay().atZone(ZoneOffset.UTC).toInstant();
        Instant endInstant = end.atStartOfDay().atZone(ZoneOffset.UTC).toInstant();
        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);
        Sort sort = Sort.by(Sort.Direction.DESC, "startDateTime");
        return candleMongoRepository.getCandlesBetweenDates(startDate, endDate, ticker, interval, sort);
    }

    public List<Candle> reloadRepositoryMoex(LocalDate defaultStartDate) throws JsonProcessingException {
        Set<String> moexTickers = candleMongoRepository.findDistinctByTickerMoex();
        List<Candle> reloadedCandles = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (String tickerJson: moexTickers) {
            JsonNode tickerNode = objectMapper.readTree(tickerJson);
            String ticker = tickerNode.get("ticker").asText();
            for (int interval: Constants.MOEX_CANDLE_SIZE) {
                LocalDate lastUpdatedDate = getLastUpdatedDate(ticker, interval, defaultStartDate);
                log.info("Getting data from MOEX: ticker {}, candle size {}, start date {}, end date {} ",
                        ticker, interval, lastUpdatedDate, currentDate);
                reloadedCandles.addAll(getMoexCandles(ticker, interval, lastUpdatedDate, currentDate));
            }
        }
        candleMongoRepository.saveAll(reloadedCandles);
        return reloadedCandles;
    }

    /* This method allows you to get the latest date of a candle
    stored in the database for a ticker and interval*/
    private LocalDate getLastUpdatedDate(String ticker, int interval, LocalDate defaultStartDate) throws JsonProcessingException {
        List<String> dates = candleMongoRepository.getLastDateForTicker(ticker, interval);
        LocalDate lastUpdatedDate;
        if (!dates.isEmpty()) {
            String dateJson = dates.get(0);
            JsonNode dateNode = objectMapper.readTree(dateJson);
            String dateString = dateNode.get("startDateTime").get("$date").asText();
            lastUpdatedDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
        } else {
            lastUpdatedDate = defaultStartDate;
        }
        return lastUpdatedDate;
    }

    public ByteArrayInputStream loadFromMoexToCsv(String ticker,
                                                   int interval,
                                                   LocalDate start,
                                                   LocalDate end) throws JsonProcessingException {
        List<Candle> candles = getMoexCandles(ticker, interval, start, end);
        return csvFileGenerator.writeCandlesToCsv(candles);
    }

    public ByteArrayInputStream loadFromAlphaVantageToCsv(String ticker,
                                                          int interval,
                                                          String apikey,
                                                          LocalDate start,
                                                          LocalDate end) throws JsonProcessingException {
        List<Candle> candles = getAlphaVantageCandles(ticker, interval, apikey, start, end);
        return csvFileGenerator.writeCandlesToCsv(candles);
    }

    public ByteArrayInputStream loadFromRepoToCsv(String ticker,
                                                  int interval,
                                                  LocalDate start,
                                                  LocalDate end) throws JsonProcessingException {
        List<Candle> candles = getCandlesFromDatabase(ticker, interval, start, end);
        return csvFileGenerator.writeCandlesToCsv(candles);
    }
}
