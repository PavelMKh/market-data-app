package com.pavelkhomenko.marketdata.mapping.commodities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkhomenko.marketdata.entity.Commodity;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CommodityPriceMapping {
    private final HttpRequestClient httpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public CommodityPriceMapping(HttpRequestClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    private String getPriceJson(String commodity, String interval, String apikey) {
        URI getPrice = URI.create("https://www.alphavantage.co/query?function=" + commodity +
                "&interval=" + interval + "&apikey=" + apikey);
        return httpClient.getResponseBody(getPrice);
    }

    public List<Commodity> getCommodityPrices(String commodity, String interval, String apikey) throws JsonProcessingException {
        String priceJson = getPriceJson(commodity, interval, apikey);
        JsonNode priceNodes = objectMapper.readTree(priceJson).get("data");
        return StreamSupport.stream(priceNodes.spliterator(), true)
                .parallel()
                .map(priceNode -> {
                    return new Commodity(commodity + priceNode.get("date").asText(),
                            commodity,
                            LocalDate.parse(priceNode.get("date").asText()),
                            interval,
                            Float.parseFloat(priceNode.get("value").asText()));
                })
                .collect(Collectors.toList());
    }
}
