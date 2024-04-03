package com.pavelkhomenko.marketdata.mapping.candles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkhomenko.marketdata.Constants;
import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.exceptions.CandleProcessingException;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlphaVantageCandleProcessor {
    private final HttpRequestClient client;

    private String getCandlesJson(String ticker, String apikey, String month,
                                      int interval) {
        URI uri = URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" +
                ticker + "&interval=" + interval + "min&month=" + month + "&outputsize=full&apikey="+apikey);
        return client.getResponseBody(uri);
    }

    public List<Candle> getCandleSet(String ticker, int interval, String apikey, LocalDate start, LocalDate end) {
        List<String> periods = getTimeIntervals(start, end);
        List<Candle> stockCandles = new CopyOnWriteArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String candlesKey = "Time Series (" + interval + "min)";
        periods.parallelStream().forEach(period -> {
            JsonNode candlesJson;
            try {
                candlesJson = objectMapper.readTree(getCandlesJson(ticker, apikey, period, interval))
                        .get(candlesKey);
            } catch (JsonProcessingException e) {
                throw new CandleProcessingException("An error has occurred during processing external server data");
            }
            JsonNode finalCandlesJson = candlesJson;
            List<String> fieldNamesList = new ArrayList<>();
            finalCandlesJson.fieldNames().forEachRemaining(fieldNamesList::add);
            List<Candle> forDateCandles = fieldNamesList.parallelStream()
                    .map(date -> buildCandleFromJson(finalCandlesJson, date, ticker, interval))
                    .toList();
            stockCandles.addAll(forDateCandles);
        });
        stockCandles.sort(Comparator.comparing(Candle::getStartDateTime));
        return stockCandles;
    }

    private Candle buildCandleFromJson(JsonNode candlesJson, String date, String ticker, int interval) {
        return Candle.builder()
                .startDateTime(OffsetDateTime.parse(date, Constants.CANDLES_DATETIME_FORMATTER))
                .open(Float.parseFloat(candlesJson.get(date).get("1. open").toString()
                        .replace("\"", "")))
                .max(Float.parseFloat(candlesJson.get(date).get("2. high").toString()
                        .replace("\"", "")))
                .min(Float.parseFloat(candlesJson.get(date).get("3. low").toString()
                        .replace("\"", "")))
                .close(Float.parseFloat(candlesJson.get(date).get("4. close").toString()
                        .replace("\"", "")))
                .volume(Float.parseFloat(candlesJson.get(date).get("5. volume").toString()
                        .replace("\"", "")))
                .source("AlphaVantage")
                .interval(interval)
                .id(LocalDateTime.parse(date, Constants.CANDLES_DATETIME_FORMATTER) + ticker + interval)
                .ticker(ticker)
                .build();
    }

    private List<String> getTimeIntervals(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        List<String> months = new ArrayList<>();
        while (startDate.isBefore(endDate)) {
            months.add(startDate.format(formatter));
            startDate = startDate.plusMonths(1);
        }
        months.add(endDate.format(formatter));
        return months;
    }

}
