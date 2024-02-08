package com.pavelkhomenko.marketdata.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.httpclients.HttpRequestClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class MoexCandleProcessor {
    private JsonArray getCandlesJson(String ticker, int interval, LocalDate start, LocalDate end) {
        URI candlesUri = URI.create("https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/" +
                ticker + "/candles.json?iss.json=compact&interval=" + interval +
                "&from=" + start + "&till=" + end);
        HttpRequestClient client = new HttpRequestClient();
        String responseBody = client.getResponseBody(candlesUri);
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        return jsonElement.getAsJsonObject()
                .get("candles").getAsJsonObject()
                .get("data").getAsJsonArray();
    }
    private Set<Candle> candlesSetFromJson(String ticker, int interval, LocalDate start, LocalDate end) {
        JsonArray candlesJson = getCandlesJson(ticker, interval, start, end);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Type listType = new TypeToken<List<String>>() {}.getType();
        Set<Candle> candles = new TreeSet<>((candle1, candle2) -> candle1.getStartDateTime()
                .isBefore(candle2.getStartDateTime()) ? -1 :
                candle1.getStartDateTime().isEqual(candle2.getStartDateTime()) ? 0 : 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (JsonElement elem: candlesJson){
            List<String> arr = gson.fromJson(elem, listType);
            try {
                candles.add(Candle.builder()
                        .startDateTime(LocalDateTime.parse(arr.get(6), formatter))
                        .open(Float.parseFloat(arr.get(0)))
                        .max(Float.parseFloat(arr.get(2)))
                        .min(Float.parseFloat(arr.get(3)))
                        .close(Float.parseFloat(arr.get(1)))
                        .volume(Float.parseFloat(arr.get(5)))
                        .build());
            } catch (NullPointerException e) {
                log.warn("No data for the period {}", arr.get(1));
            }
        }
        return candles;
    }

    /* This method allows to get candles for any interval
    * the interval variable is responsible for the candle size.
    * Candle size - 1 (1 minute), 10 (10 minutes), 60 (1 hour),
    * 24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)*/
    public Set<Candle> collectAllCandlesSet(String ticker, int interval, LocalDate start, LocalDate end){
        var periods = getTimeIntervals(start, end);
        Set<Candle> stockCandles = new TreeSet<>((candle1, candle2) -> candle1.getStartDateTime()
                .isBefore(candle2.getStartDateTime()) ? -1 :
                candle1.getStartDateTime().isEqual(candle2.getStartDateTime()) ? 0 : 1);
        for (List<String> period: periods) {
            stockCandles.addAll(candlesSetFromJson(ticker,
                    interval,
                    LocalDate.parse(period.get(0)),
                    LocalDate.parse(period.get(1))));
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