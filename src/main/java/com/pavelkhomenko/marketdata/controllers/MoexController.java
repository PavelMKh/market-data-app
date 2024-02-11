package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.MoexCandleProcessor;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/moex")
@Slf4j
@Validated
@RequiredArgsConstructor
public class MoexController {
    @NotNull
    private final MoexCandleProcessor requestProcessor;

    @GetMapping("/shares/{ticker}/history")
    public Set<Candle> getCandlesByTicker(@RequestParam("candlesize") int interval,
                                          @RequestParam("startdate") @PastOrPresent LocalDate start,
                                          @RequestParam("enddate") @PastOrPresent LocalDate end,
                                          @PathVariable @NotBlank @NotEmpty String ticker) throws JsonProcessingException {
        return requestProcessor.getCandleSet(ticker, interval, start, end);
    }
}
