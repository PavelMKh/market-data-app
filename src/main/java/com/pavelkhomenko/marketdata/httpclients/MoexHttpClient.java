package com.pavelkhomenko.marketdata.httpclients;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@Slf4j
public class MoexHttpClient {
    public String getCandleHistory(String ticker, int interval, LocalDate start, LocalDate end) {
        String moexStockHistoryData = "https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/";
        URI uri = URI.create(moexStockHistoryData + ticker + "/candles.json?iss.json=compact&interval=" + interval +
                "&from=" + start + "&till=" + end);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();

        HttpRequest request = requestBuilder
                .GET()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(uri)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> response = client.send(request, handler);
            return response.body();
        } catch (IOException | InterruptedException e) {
            log.warn("Error data receiving!");
        }
        return null;
    }


}
