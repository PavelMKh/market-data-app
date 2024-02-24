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
@RequestMapping("/repo")
@Validated
@RequiredArgsConstructor
public class RepositoryController {
    private final CandleHistoryService candleHistoryService;
    @GetMapping("/shares/{ticker}/history")
    public List<Candle> getCandlesHistoryFromRepository(@RequestParam("candlesize") int interval,
                                                        @RequestParam("startdate") LocalDate startDate,
                                                        @RequestParam("enddate") LocalDate endDate,
                                                        @PathVariable @NotBlank @NotEmpty String ticker) {
        return candleHistoryService.getCandlesFromDatabase(ticker, interval, startDate, endDate);
    }

    @GetMapping("/reload/moex")
    public List<Candle> reloadRepositoryMoex(@RequestParam("defaultStartDate") LocalDate defaultStartDate)
            throws JsonProcessingException {
        return candleHistoryService.reloadRepositoryMoex(defaultStartDate);
    }
}
