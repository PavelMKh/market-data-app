package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.httpclients.HttpRequestClient;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlphaVantageCandleProcessor {

    @NotNull
    private final HttpRequestClient client;
    private String getCandlesJson(String ticker, String apikey, String month,
                                      int interval) {
        URI uri = URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" +
                ticker + "&interval=" + interval + "min&month=" + month + "&outputsize=full&apikey="+apikey);
        return client.getResponseBody(uri);
    }

    public Set<Candle> getCandleSet(String ticker, int interval, String apikey, LocalDate start, LocalDate end) throws JsonProcessingException {
        List<String> periods = getTimeIntervals(start, end);
        Set<Candle> stockCandles = new TreeSet<>((candle1, candle2) -> candle1.getStartDateTime()
                .isBefore(candle2.getStartDateTime()) ? -1 :
                candle1.getStartDateTime().isEqual(candle2.getStartDateTime()) ? 0 : 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        String candlesKey = "Time Series (" + interval + "min)";
        for (String period: periods) {
            JsonNode candlesJson = objectMapper.readTree(getCandlesJson(ticker, apikey, period, interval))
                    .get(candlesKey);
            Iterator<String> fieldNames = candlesJson.fieldNames();
            while (fieldNames.hasNext()) {
                String date = fieldNames.next();
                try {
                    stockCandles.add(Candle.builder()
                            .startDateTime(LocalDateTime.parse(date, formatter))
                            .open(Float.parseFloat(candlesJson.get(date).get("1. open").toString().replace("\"", "")))
                            .max(Float.parseFloat(candlesJson.get(date).get("2. high").toString().replace("\"", "")))
                            .min(Float.parseFloat(candlesJson.get(date).get("3. low").toString().replace("\"", "")))
                            .close(Float.parseFloat(candlesJson.get(date).get("4. close").toString().replace("\"", "")))
                            .volume(Float.parseFloat(candlesJson.get(date).get("5. volume").toString().replace("\"", "")))
                            .build());
                } catch (NullPointerException e) {
                    log.warn("No data for the period {}", date);
                }
            }
        }
        return stockCandles;
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
