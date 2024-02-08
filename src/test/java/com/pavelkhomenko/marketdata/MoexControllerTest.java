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
public class MoexControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCandlesHistoryTest() throws Exception {
        String candleHistoryJson = new String(Files.readAllBytes(Paths.get("src/test/resources/moex.json")));

        mockMvc.perform(get("/moex/shares/SBER/history?candlesize=60&startdate=2024-01-29&enddate=2024-01-29"))
                .andExpect(status().isOk())
                .andExpect(content().json(candleHistoryJson));
    }


}
