package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.CandleHistoryService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/global")
@Validated
@RequiredArgsConstructor
public class AlphaVantageController {
    @NotNull
    private final CandleHistoryService candleHistoryService;

    @GetMapping("/shares/{ticker}/history")
    public List<Candle> getCandlesHistory(@RequestParam("candlesize") int interval,
                                          @RequestParam("startdate") LocalDate startDate,
                                          @RequestParam("enddate") LocalDate endDate,
                                          @RequestParam("apikey") String apikey,
                                          @PathVariable @NotBlank @NotEmpty String ticker) throws JsonProcessingException {
        return candleHistoryService.getAlphaVantageCandles(ticker, interval, apikey, startDate, endDate);
    }


}
