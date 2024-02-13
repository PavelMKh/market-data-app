package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.aspects.LoggingAspect;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.AlphaVantageCandleProcessor;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/global")
@Validated
@RequiredArgsConstructor
public class AlphaVantageController {
    @NotNull
    private final AlphaVantageCandleProcessor requestProcessor;
    /*@NotNull
    private final LoggingAspect loggingAspect;*/

    @GetMapping("/shares/{ticker}/history")
    public Set<Candle> getCandlesHistory(@RequestParam("candlesize") int interval,
                                         @RequestParam("startdate") LocalDate startDate,
                                         @RequestParam("enddate") LocalDate endDate,
                                         @RequestParam("apikey") String apikey,
                                         @PathVariable @NotBlank @NotEmpty String ticker) throws JsonProcessingException {
        if (LocalDate.now().isBefore(startDate) || LocalDate.now().isBefore(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Future dates cannot be query parameters");
        }
        if (interval != 1 && interval != 15 && interval != 30 && interval != 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The candle size is not valid. Valid values: " +
                    "1 (1 minute), 5 (5 minutes), 15 (15 minutes), 30 (30 minutes), 60 (60 minutes)");
        }
        return requestProcessor.getCandleSet(ticker, interval, apikey, startDate, endDate);
    }


}
