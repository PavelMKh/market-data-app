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
@RequestMapping("/moex")
@Validated
@RequiredArgsConstructor
public class MoexController {
    @NotNull
    private final CandleHistoryService candleHistoryService;

    @GetMapping("/shares/{ticker}/history")
    public List<Candle> getCandlesByTicker(@RequestParam("candlesize") int interval,
                                           @RequestParam("startdate") LocalDate start,
                                           @RequestParam("enddate") LocalDate end,
                                           @PathVariable String ticker) throws JsonProcessingException {
        return candleHistoryService.getMoexCandles(ticker, interval, start, end);
    }
}
