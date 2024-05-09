package com.pavelkhomenko.marketdata.mapping.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pavelkhomenko.marketdata.entity.CashFlow;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class CashFlowMapping {
    private final HttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;
    @Autowired
    public CashFlowMapping(HttpRequestClient httpRequestClient) {
        this.httpRequestClient = httpRequestClient;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public List<CashFlow> getCfList(String ticker, String apiKey) throws JsonProcessingException {
        String pnlJson = getCfJson(ticker, apiKey);
        return Stream.of(
                        processCf(objectMapper.readTree(pnlJson).get("quarterlyReports"), ticker, "quarter"),
                        processCf(objectMapper.readTree(pnlJson).get("annualReports"), ticker, "annual")
                )
                .parallel()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<CashFlow> processCf(JsonNode report, String ticker, String type) {
        return StreamSupport.stream(report.spliterator(), true)
                .parallel()
                .map(cfReport -> {
                    CashFlow cf;
                    try {
                        cf = objectMapper.treeToValue(cfReport, CashFlow.class);
                        cf.setType(type);
                        cf.setId(ticker + "CF" + cf.getFiscalDateEnding() + type);
                        cf.setTicker(ticker);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return cf;
                })
                .collect(Collectors.toList());
    }

    private String getCfJson(String ticker, String apiKey) {
        URI uri = URI.create(
                "https://www.alphavantage.co/query?function=CASH_FLOW&symbol=" +
                        ticker + "&apikey="+apiKey);
        return httpRequestClient.getResponseBody(uri);
    }
}
