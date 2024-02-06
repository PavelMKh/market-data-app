package com.pavelkhomenko.marketdata.controllers;

import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.MoexCandleProcessor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/stocks")
@Slf4j
@Validated
public class MoexController {
    @GetMapping("/{ticker}/history")
    public Set<Candle> getCandlesByTicker(@RequestParam("candlesize") int interval,
                                          @RequestParam("startdate") @Past LocalDate start,
                                          @RequestParam("enddate") @PastOrPresent LocalDate end,
                                          @PathVariable @NotBlank @NotEmpty String ticker) {
        return MoexCandleProcessor.stockDailyCandles(ticker, interval, start, end);
    }
}
