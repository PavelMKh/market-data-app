package com.pavelkhomenko.marketdata.mapping.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import com.pavelkhomenko.marketdata.util.HttpRequestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class IncomeStatementProcessing {
    private final HttpRequestClient httpRequestClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public IncomeStatementProcessing(HttpRequestClient httpRequestClient) {
        this.httpRequestClient = httpRequestClient;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public List<IncomeStatement> getPnlList(String ticker, String apiKey) throws JsonProcessingException {
        String pnlJson = getIncomeStatementJson(ticker, apiKey);
        return Stream.of(
                        processReport(objectMapper.readTree(pnlJson).get("quarterlyReports"), ticker, "quarter"),
                        processReport(objectMapper.readTree(pnlJson).get("annualReports"), ticker, "annual")
                )
                .parallel()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<IncomeStatement> processReport(JsonNode report, String ticker, String type) {
        return StreamSupport.stream(report.spliterator(), true)
                .parallel()
                .map(pnlReport -> {
                    IncomeStatement pnl;
                    try {
                        pnl = objectMapper.treeToValue(pnlReport, IncomeStatement.class);
                        pnl.setType(type);
                        pnl.setId(ticker + "PNL" + pnl.getFiscalDateEnding() + type);
                        pnl.setTicker(ticker);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return pnl;
                })
                .collect(Collectors.toList());
    }

    private String getIncomeStatementJson(String ticker, String apiKey) {
        URI uri = URI.create(
                "https://www.alphavantage.co/query?function=INCOME_STATEMENT&symbol=" +
                        ticker + "&apikey="+apiKey);
        return httpRequestClient.getResponseBody(uri);
    }
}
