package com.pavelkhomenko.marketdata.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class HttpRequestClient {
    public String getResponseBody(URI uri) {
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
