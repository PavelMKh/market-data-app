package com.pavelkhomenko.marketdata.mapping.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class BalanceSheetProcessing {
    private final HttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public BalanceSheetProcessing(HttpRequestClient httpRequestClient) {
        this.httpRequestClient = httpRequestClient;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public List<BalanceSheet> getBsList(String ticker, String apiKey) throws JsonProcessingException {
        String bsJson = getBsJson(ticker, apiKey);
        return Stream.of(
                        processReport(objectMapper.readTree(bsJson).get("quarterlyReports"), ticker, "quarter"),
                        processReport(objectMapper.readTree(bsJson).get("annualReports"), ticker, "annual"))
                .parallel()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<BalanceSheet> processReport(JsonNode report, String ticker, String type) {
        return StreamSupport.stream(report.spliterator(), true)
                .parallel()
                .map(bsReport -> {
                    BalanceSheet bs;
                    try {
                        bs = objectMapper.treeToValue(bsReport, BalanceSheet.class);
                        bs.setType(type);
                        bs.setId(ticker + "BS" + bs.getFiscalDateEnding() + type);
                        bs.setTicker(ticker);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return bs;
                })
                .collect(Collectors.toList());
    }

    private String getBsJson(String ticker, String apiKey) {
        URI uri = URI.create(
                "https://www.alphavantage.co/query?function=BALANCE_SHEET&symbol=" +
                        ticker + "&apikey="+apiKey);
        return httpRequestClient.getResponseBody(uri);
    }
}
