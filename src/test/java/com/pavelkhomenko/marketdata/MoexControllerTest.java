package com.pavelkhomenko.marketdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MoexControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/moex.json")));
        String url = "/moex/shares/SBER/history?candlesize=60&startdate=2024-01-29&enddate=2024-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }

    @Test
    public void getCandlesHistoryWithIncorrectStartDateTest() throws Exception {
        String url = "/moex/shares/SBER/history?candlesize=60&startdate=2028-01-29&enddate=2024-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Future dates cannot be query parameters",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    public void getCandlesHistoryWithIncorrectEndDateTest() throws Exception {
        String url = "/moex/shares/SBER/history?candlesize=60&startdate=2024-01-29&enddate=2028-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Future dates cannot be query parameters",
                        result.getResponse().getErrorMessage()));
    }

    @Test
    public void getCandlesHistoryWithIncorrectCandleSizeTest() throws Exception {
        String url = "/moex/shares/SBER/history?candlesize=93&startdate=2024-01-29&enddate=2024-01-29";
        mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("The candle size is not valid. Valid values: " +
                                "1 (1 minute), 10 (10 minutes), 60 (1 hour), " +
                                "24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)",
                        result.getResponse().getErrorMessage()));
    }


}
