package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.CandleHistoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class CandlesHistoryController {
    private final CandleHistoryService candleHistoryService;

    @GetMapping("/moex/shares/{ticker}/history")
    public List<Candle> getCandlesHistoryMoex(@RequestParam("candlesize") int interval,
                                           @RequestParam("startdate") LocalDate start,
                                           @RequestParam("enddate") LocalDate end,
                                           @PathVariable String ticker) throws JsonProcessingException {
        return candleHistoryService.getMoexCandles(ticker, interval, start, end);
    }

    @GetMapping("/global/shares/{ticker}/history")
    public List<Candle> getCandlesHistoryAlphaVantage(@RequestParam("candlesize") int interval,
                                          @RequestParam("startdate") LocalDate startDate,
                                          @RequestParam("enddate") LocalDate endDate,
                                          @RequestParam("apikey") String apikey,
                                          @PathVariable @NotBlank @NotEmpty String ticker) throws JsonProcessingException {
        return candleHistoryService.getAlphaVantageCandles(ticker, interval, apikey, startDate, endDate);
    }
    @GetMapping("/repo/shares/{ticker}/history")
    public List<Candle> getCandlesHistoryFromRepository(@RequestParam("candlesize") int interval,
                                                        @RequestParam("startdate") LocalDate startDate,
                                                        @RequestParam("enddate") LocalDate endDate,
                                                        @PathVariable @NotBlank @NotEmpty String ticker) {
        return candleHistoryService.getCandlesFromDatabase(ticker, interval, startDate, endDate);
    }

    @GetMapping("/repo/reload/moex")
    public List<Candle> reloadRepositoryMoex(@RequestParam("defaultStartDate") LocalDate defaultStartDate)
            throws JsonProcessingException {
        return candleHistoryService.reloadRepositoryMoex(defaultStartDate);
    }
}
