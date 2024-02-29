package com.pavelkhomenko.marketdata.candleprocessing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.httpclient.HttpRequestClient;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoexCandleProcessor {

    @NotNull
    private final HttpRequestClient client;

    private String getCandlesJson(String ticker, int interval, LocalDate start, LocalDate end) {
        URI candlesUri = URI.create("https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/" +
                ticker + "/candles.json?iss.json=compact&interval=" + interval +
                "&from=" + start + "&till=" + end);
        return client.getResponseBody(candlesUri);
    }

    public List<Candle> getCandleSet(String ticker, int interval, LocalDate start, LocalDate end) throws JsonProcessingException {
        var periods = getTimeIntervals(start, end);
        List<Candle> stockCandles = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        ZoneOffset zoneOffset = ZoneOffset.of("+00:00");
        for (List<String> period: periods) {
            ArrayNode candlesJson = (ArrayNode) objectMapper.readTree(getCandlesJson(ticker, interval, LocalDate.parse(period.get(0)),
                    LocalDate.parse(period.get(1)))).get("candles").get("data");
            candlesJson.forEach(element -> {
                stockCandles.add(Candle.builder()
                        .startDateTime(Date.from(LocalDateTime.parse(element.get(6).asText(), formatter)
                                .toInstant(zoneOffset)))
                        .open(Float.parseFloat(element.get(0).asText()))
                        .max(Float.parseFloat(element.get(2).asText()))
                        .min(Float.parseFloat(element.get(3).asText()))
                        .close(Float.parseFloat(element.get(1).asText()))
                        .volume(Float.parseFloat(element.get(5).asText()))
                        .source("MOEX")
                        .interval(interval)
                        .ticker(ticker)
                        .id(LocalDateTime.parse(element.get(6).asText(), formatter)+ticker+interval)
                        .build());
            });
        }
        return stockCandles;
    }

    /* Data can be requested if interval doesn't exceed 100 days
    * If the client interval exceeds 99 days, this function allows
    * to split the client interval into several intervals of no more than 99 days*/
    private List<List<String>> getTimeIntervals(LocalDate startDate, LocalDate endDate){
        List<List<String>> result = new ArrayList<>();
        long dayInterval = ChronoUnit.DAYS.between(startDate, endDate);
        int numberOfPeriods = (int) (dayInterval / 100);
        for (int i = 0; i < numberOfPeriods; i++){
            List<String> startEndDate = new ArrayList<>();
            LocalDate endOfInterval = startDate.plusDays(100);
            startEndDate.add(startDate.toString());
            startEndDate.add(endOfInterval.toString());
            result.add(List.copyOf(startEndDate));
            startDate = endOfInterval;
            startEndDate.clear();
        }
        List<String> preFinalInterval = List.of(startDate.toString(), startDate.plusDays(50).toString());
        startDate = startDate.plusDays(50);
        result.add(preFinalInterval);
        List<String> finalInterval = List.of(startDate.toString(), endDate.toString());
        result.add(finalInterval);
        return result;
    }

}