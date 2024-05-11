package com.pavelkhomenko.marketdata.mapping.candles;

import com.fasterxml.jackson.databind.JsonNode;
import com.pavelkhomenko.marketdata.Constants;
import com.pavelkhomenko.marketdata.entity.Candle;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class CandleBuilder {
    public Candle buildCandleFromJsonGlobal(JsonNode candlesJson, String date, String ticker, int interval) {
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

    public Candle buildDailyCandleFromJsonGlobal(JsonNode candlesJson, String date, String ticker) {
        return Candle.builder()
                .startDateTime(OffsetDateTime.parse(date + " 00:00:00",
                        Constants.CANDLES_DATETIME_FORMATTER))
                .open(Float.parseFloat(candlesJson.get("1. open").toString()
                        .replace("\"", "")))
                .max(Float.parseFloat(candlesJson.get("2. high").toString()
                        .replace("\"", "")))
                .min(Float.parseFloat(candlesJson.get("3. low").toString()
                        .replace("\"", "")))
                .close(Float.parseFloat(candlesJson.get("4. close").toString()
                        .replace("\"", "")))
                .volume(Float.parseFloat(candlesJson.get("5. volume").toString()
                        .replace("\"", "")))
                .source("AlphaVantage")
                .interval(24)
                .id(LocalDateTime.parse(date + " 00:00:00", Constants.CANDLES_DATETIME_FORMATTER) + ticker + "24")
                .ticker(ticker)
                .build();
    }

    public Candle buildCandleFromJsonMoex(JsonNode jsonCandle, String ticker, int interval) {
        return Candle.builder()
                .startDateTime(OffsetDateTime.of(LocalDateTime.parse(jsonCandle.get(6).asText(), Constants.CANDLES_DATETIME_FORMATTER),
                        ZoneOffset.UTC))
                .open(Float.parseFloat(jsonCandle.get(0).asText()))
                .max(Float.parseFloat(jsonCandle.get(2).asText()))
                .min(Float.parseFloat(jsonCandle.get(3).asText()))
                .close(Float.parseFloat(jsonCandle.get(1).asText()))
                .volume(Float.parseFloat(jsonCandle.get(5).asText()))
                .source("MOEX")
                .interval(interval)
                .ticker(ticker)
                .id(LocalDateTime.parse(jsonCandle.get(6).asText(), Constants.CANDLES_DATETIME_FORMATTER) + ticker + interval)
                .build();
    }
}
