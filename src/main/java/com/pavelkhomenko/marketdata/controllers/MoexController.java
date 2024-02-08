package com.pavelkhomenko.marketdata.controllers;

import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.MoexCandleProcessor;
import jakarta.validation.constraints.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/moex")
@Slf4j
@Validated
public class MoexController {
    @GetMapping("/shares/{ticker}/history")
    public Set<Candle> getCandlesByTicker(@RequestParam("candlesize") int interval,
                                          @RequestParam("startdate") @PastOrPresent LocalDate start,
                                          @RequestParam("enddate") @PastOrPresent LocalDate end,
                                          @PathVariable @NotBlank @NotEmpty String ticker) {
        return new MoexCandleProcessor().collectAllCandlesSet(ticker, interval, start, end);
    }
}
