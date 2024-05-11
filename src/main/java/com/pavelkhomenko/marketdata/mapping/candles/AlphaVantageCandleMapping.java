package com.pavelkhomenko.marketdata.mapping.candles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.exceptions.ProcessingException;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlphaVantageCandleMapping {
    private final HttpRequestClient client;
    private final ObjectMapper objectMapper;
    private final CandleBuilder candleBuilder;

    private String getCandlesJson(String ticker, String apikey, String month,
                                      int interval) {
        URI uri = URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" +
                ticker + "&interval=" + interval + "min&month=" + month + "&outputsize=full&apikey="+apikey);
        return client.getResponseBody(uri);
    }

    private String getDailyCandlesJson(String ticker, String apikey) {
        URI uri = URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" +
                ticker + "&outputsize=full&apikey="+apikey);
        return client.getResponseBody(uri);
    }

    public List<Candle> getCandleSet(String ticker, int interval, String apikey, LocalDate start, LocalDate end) {
        List<String> periods = getTimeIntervals(start, end);
        List<Candle> stockCandles = new CopyOnWriteArrayList<>();
        String candlesKey = "Time Series (" + interval + "min)";
        periods.parallelStream().forEach(period -> {
            JsonNode candlesJson;
            try {
                candlesJson = objectMapper.readTree(getCandlesJson(ticker, apikey, period, interval))
                        .get(candlesKey);
            } catch (JsonProcessingException e) {
                throw new ProcessingException("An error has occurred during processing external server data");
            }
            JsonNode finalCandlesJson = candlesJson;
            List<String> fieldNamesList = new ArrayList<>();
            finalCandlesJson.fieldNames().forEachRemaining(fieldNamesList::add);
            List<Candle> forDateCandles = fieldNamesList.parallelStream()
                    .map(date -> candleBuilder.buildCandleFromJsonGlobal(finalCandlesJson, date, ticker, interval))
                    .toList();
            stockCandles.addAll(forDateCandles);
        });
        stockCandles.sort(Comparator.comparing(Candle::getStartDateTime));
        return stockCandles;
    }

    public List<Candle> getDailyCandles(String ticker, String apikey) throws JsonProcessingException {
        JsonNode candlesJson = objectMapper.readTree(getDailyCandlesJson(ticker, apikey))
                .get("Time Series (Daily)");
        List<Candle> candles = new ArrayList<>();
        candlesJson.fields().forEachRemaining(entry -> {
            String date = entry.getKey();
            JsonNode candleNode = entry.getValue();
            Candle candle = candleBuilder.buildDailyCandleFromJsonGlobal(candleNode, date, ticker);
            candles.add(candle);}
        );
        return candles;
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
