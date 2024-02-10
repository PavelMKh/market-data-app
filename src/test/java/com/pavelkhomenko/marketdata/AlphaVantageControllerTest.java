package com.pavelkhomenko.marketdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AlphaVantageControllerTest {
   /* @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/alphavantage.json")));
        String token = ""; //input your token
        mockMvc.perform(get("https://localhost:8080/global/shares/IBM/history?candlesize=60&startdate=2023-03-01&enddate=2023-03-01&apikey=" + token))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }*/
}
