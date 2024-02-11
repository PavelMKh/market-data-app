package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.AlphaVantageCandleProcessor;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/global")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AlphaVantageController {
    @NotNull
    private final AlphaVantageCandleProcessor requestProcessor;

    @GetMapping("/shares/{ticker}/history")
    public Set<Candle> getCandlesHistory(@RequestParam("candlesize") int interval,
                                         @RequestParam("startdate") @Past LocalDate startDate,
                                         @RequestParam("enddate") @PastOrPresent LocalDate endDate,
                                         @RequestParam("apikey") String apikey,
                                         @PathVariable @NotBlank @NotEmpty String ticker) throws JsonProcessingException {
        return requestProcessor.getCandleSet(ticker, interval, apikey, startDate, endDate);
    }


}
