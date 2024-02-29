package com.pavelkhomenko.marketdata;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CandleHistoryServiceTest {
    /*@Autowired
    private MockMvc mockMvc;
    private String alphaVantageKey = ""; //input your API-key

    @Test
    public void getCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/alphavantage.json")));
        String url = "/global/shares/IBM/history?candlesize=60&" +
                "startdate=2023-03-01&enddate=2023-03-01&apikey=" + alphaVantageKey;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectStartDateTest() throws Exception {
        String errorJson = "{\"error\":\"Future dates cannot be query parameters\"}";
        String url = "/global/shares/IBM/history?candlesize=60&" +
                "startdate=2027-03-01&enddate=2023-03-01&apikey=" + alphaVantageKey;
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectEndDateTest() throws Exception {
        String errorJson = "{\"error\":\"Future dates cannot be query parameters\"}";
        String url = "/global/shares/IBM/history?candlesize=60&" +
                "startdate=2023-03-01&enddate=2027-03-01&apikey=" + alphaVantageKey;
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectCandleSizeTest() throws Exception {
        String errorJson = "{\"error\":\"The candle size is not valid. Valid values: 1 (1 minute), 5 (5 minutes), " +
                "15 (1 minutes), 30 (30 minutes), 60 (60 minutes)\"}";
        String url = "/global/shares/IBM/history?candlesize=75&startdate=2023-03-01&" +
                "enddate=2023-03-01&apikey=" + alphaVantageKey;
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getMoexCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/moex.json")));
        String url = "/moex/shares/SBER/history?candlesize=60&startdate=2024-01-29&enddate=2024-01-30";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }

    @Test
    public void getMoexCandlesHistoryWithIncorrectStartDateTest() throws Exception {
        String errorJson = "{\"error\":\"Future dates cannot be query parameters\"}";
        String url = "/moex/shares/SBER/history?candlesize=60&startdate=2028-01-29&enddate=2024-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getMoexCandlesHistoryWithIncorrectEndDateTest() throws Exception {
        String errorJson = "{\"error\":\"Future dates cannot be query parameters\"}";
        String url = "/moex/shares/SBER/history?candlesize=60&startdate=2024-01-29&enddate=2028-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getMoexCandlesHistoryWithIncorrectCandleSizeTest() throws Exception {
        String errorJson = "{\"error\":\"The candle size is not valid. Valid values: 1 (1 minute), " +
                "10 (10 minutes), 60 (1 hour), 24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)\"}";
        String url = "/moex/shares/SBER/history?candlesize=93&startdate=2024-01-29&enddate=2024-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorJson));
    }

    @Test
    public void getRepoGlobalCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/repoav.json")));
        String url = "/repo/shares/IBM/history?candlesize=60&startdate=2023-02-21&enddate=2023-03-15";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }

    @Test
    public void getRepoMoexlCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/repomoex.json")));
        String url = "/repo/shares/SBER/history?candlesize=60&startdate=2024-01-29&enddate=2024-01-31";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }

    @Test
    public void getMoexCandlesToCsvTest() throws Exception {
        String url = "/moex/shares/SBER/export-to-csv?candlesize=60&startdate=2023-01-29&enddate=2024-01-30";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=SBER_60_2023-01-29_2024-01-30.csv"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/csv")));

    }

    @Test
    public void getRepoCandlesToCsvTest() throws Exception {
        String url = "/repo/shares/SBER/export-to-csv?candlesize=60&startdate=2023-01-29&enddate=2024-01-30";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=SBER_60_2023-01-29_2024-01-30.csv"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/csv")));

    }

    @Test
    public void getAlphaVantageCandlesToCsvTest() throws Exception {
        String url = "/global/shares/IBM/export-to-csv?candlesize=60&" +
                "startdate=2023-03-01&enddate=2023-03-01&apikey=" + alphaVantageKey;
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=IBM_60_2023-03-01_2023-03-01.csv"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/csv")));

    }*/
}
