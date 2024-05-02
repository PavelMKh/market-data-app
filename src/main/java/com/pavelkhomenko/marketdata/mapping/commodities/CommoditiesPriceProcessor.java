package com.pavelkhomenko.marketdata.mapping.commodities;

import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class CommoditiesPriceProcessor {
    private final HttpRequestClient httpClient;

    @Autowired
    public CommoditiesPriceProcessor(HttpRequestClient httpClient) {
        this.httpClient = httpClient;
    }

    private String getPriceJson(String commodity, String interval, String apikey) {
        URI getPrice = URI.create("https://www.alphavantage.co/query?function=" + commodity +
                "&interval=" + interval + "&apikey=" + apikey);
        return httpClient.getResponseBody(getPrice);
    }
}
