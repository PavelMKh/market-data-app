package com.pavelkhomenko.marketdata.mapping.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class CompanyOverviewMapping {
    private final HttpRequestClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Company getOverview(String ticker, String apiKey)
            throws JsonProcessingException {
        JsonNode companyJson = objectMapper.readTree(getOverviewJson(ticker,
                apiKey));
        return objectMapper.treeToValue(companyJson, Company.class);
    }

    private String getOverviewJson(String ticker, String apiKey) {
        URI uri = URI.create(
                "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" +
                        ticker + "&apikey="+apiKey);
        return client.getResponseBody(uri);
    }
}
