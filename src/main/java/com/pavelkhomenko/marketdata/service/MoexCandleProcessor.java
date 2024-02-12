package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

    public Set<Candle> getCandleSet(String ticker, int interval, LocalDate start, LocalDate end) throws JsonProcessingException {
        var periods = getTimeIntervals(start, end);
        Set<Candle> stockCandles = new TreeSet<>((candle1, candle2) -> candle1.getStartDateTime()
                .isBefore(candle2.getStartDateTime()) ? -1 :
                candle1.getStartDateTime().isEqual(candle2.getStartDateTime()) ? 0 : 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        for (List<String> period: periods) {
            ArrayNode candlesJson = (ArrayNode) objectMapper.readTree(getCandlesJson(ticker, interval, LocalDate.parse(period.get(0)),
                        LocalDate.parse(period.get(1)))).get("candles").get("data");
            candlesJson.forEach(element -> {
                stockCandles.add(Candle.builder()
                            .startDateTime(LocalDateTime.parse(element.get(6).asText(), formatter))
                            .open(Float.parseFloat(element.get(0).asText()))
                            .max(Float.parseFloat(element.get(2).asText()))
                            .min(Float.parseFloat(element.get(3).asText()))
                            .close(Float.parseFloat(element.get(1).asText()))
                            .volume(Float.parseFloat(element.get(5).asText()))
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
            LocalDate endOfInterval = startDate.plusDays(99);
            startEndDate.add(startDate.toString());
            startEndDate.add(endOfInterval.toString());
            result.add(List.copyOf(startEndDate));
            startDate = endOfInterval;
            startEndDate.clear();
        }
        List<String> finalInterval = List.of(startDate.toString(), endDate.toString());
        result.add(finalInterval);
        return result;
    }

}