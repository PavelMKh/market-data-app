package com.pavelkhomenko.marketdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AlphaVantageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String token = ""; //input your API-key

    @Test
    public void getCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/alphavantage.json")));
        String url = "https://localhost:8080/global/shares/IBM/history?candlesize=60&startdate=2023-03-01&enddate=2023-03-01&apikey=" + token;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectStartDateTest() throws Exception {
        String errorJson = "{\"error\":\"Future dates cannot be query parameters\"}";
        String url = "https://localhost:8080/global/shares/IBM/history?candlesize=60&startdate=2027-03-01&enddate=2023-03-01&apikey=" + token;
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectEndDateTest() throws Exception {
        String errorJson = "{\"error\":\"Future dates cannot be query parameters\"}";
        String url = "https://localhost:8080/global/shares/IBM/history?candlesize=60&startdate=2023-03-01&enddate=2027-03-01&apikey=" + token;
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectCandleSizeTest() throws Exception {
        String errorJson = "{\"error\":\"The candle size is not valid. Valid values: 1 (1 minute), 5 (5 minutes), " +
                "15 (1 minutes), 30 (30 minutes), 60 (60 minutes)\"}";
        String url = "https://localhost:8080/global/shares/IBM/history?candlesize=75&startdate=2023-03-01&enddate=2023-03-01&apikey=" + token;
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }
}
