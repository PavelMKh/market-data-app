package com.pavelkhomenko.marketdata.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.httpclients.HttpRequestClient;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class AlphaVantageCandleProcessor {
    private JsonObject getCandlesJson(String ticker, String apikey, String month,
                                      int interval) {
        URI uri = URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" +
                ticker + "&interval=" + interval + "min&month=" + month + "&outputsize=full&apikey="+apikey);
        HttpRequestClient client = new HttpRequestClient();
        String responseBody = client.getResponseBody(uri);
        JsonElement jsonElement = JsonParser.parseString(responseBody);
        String candlesKey = "Time Series (" + interval + "min)";
        return jsonElement.getAsJsonObject()
                .get(candlesKey).getAsJsonObject();
    }

    private Set<Candle> candlesSetFromJson(String ticker, String apikey, String month,
                                           int interval) {
        JsonObject candlesJson = getCandlesJson(ticker, apikey, month, interval);
        Set<Candle> candles = new TreeSet<>((candle1, candle2) -> candle1.getStartDateTime()
                .isBefore(candle2.getStartDateTime()) ? -1 :
                candle1.getStartDateTime().isEqual(candle2.getStartDateTime()) ? 0 : 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (String key: candlesJson.keySet()){
            try {
                candles.add(Candle.builder()
                        .startDateTime(LocalDateTime.parse(key, formatter))
                        .open(Float.parseFloat(candlesJson.get(key).getAsJsonObject().get("1. open").toString().replace("\"", "")))
                        .max(Float.parseFloat(candlesJson.get(key).getAsJsonObject().get("2. high").toString().replace("\"", "")))
                        .min(Float.parseFloat(candlesJson.get(key).getAsJsonObject().get("3. low").toString().replace("\"", "")))
                        .close(Float.parseFloat(candlesJson.get(key).getAsJsonObject().get("4. close").toString().replace("\"", "")))
                        .volume(Float.parseFloat(candlesJson.get(key).getAsJsonObject().get("5. volume").toString().replace("\"", "")))
                        .build());
            } catch (NullPointerException e) {
                log.warn("No data for the period {}", key);
            }
        }
        return candles;
    }

    public Set<Candle> collectAllCandlesSet(String ticker, int interval, String apikey, LocalDate start, LocalDate end) {
        var periods = getTimeIntervals(start, end);
        Set<Candle> stockCandles = new TreeSet<>((candle1, candle2) -> candle1.getStartDateTime()
                .isBefore(candle2.getStartDateTime()) ? -1 :
                candle1.getStartDateTime().isEqual(candle2.getStartDateTime()) ? 0 : 1);
        for (String period: periods) {
            stockCandles.addAll(candlesSetFromJson(ticker, apikey, period, interval));
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
