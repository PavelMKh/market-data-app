package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.AlphaVantageCandleProcessor;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                                         @RequestParam("startdate") LocalDate startDate,
                                         @RequestParam("enddate") LocalDate endDate,
                                         @RequestParam("apikey") String apikey,
                                         @PathVariable @NotBlank @NotEmpty String ticker) throws JsonProcessingException {
        if (LocalDate.now().isBefore(startDate) || LocalDate.now().isBefore(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Future dates cannot be query parameters");
        }
        return requestProcessor.getCandleSet(ticker, interval, apikey, startDate, endDate);
    }


}
