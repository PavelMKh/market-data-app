package com.pavelkhomenko.marketdata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.Candle;
import com.pavelkhomenko.marketdata.service.MoexCandleProcessor;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                                          @RequestParam("startdate") LocalDate start,
                                          @RequestParam("enddate") LocalDate end,
                                          @PathVariable String ticker) throws JsonProcessingException {
        if (LocalDate.now().isBefore(start) || LocalDate.now().isBefore(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Future dates cannot be query parameters");
        }
        if (interval != 1 && interval != 10 && interval != 60 && interval != 24 && interval != 7 && interval != 31 &&
                interval != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The candle size is not valid. Valid values: " +
                    "1 (1 minute), 10 (10 minutes), 60 (1 hour), " +
                    "24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)");
        }
        if (ticker.isEmpty() || ticker.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticker can't be empty or blank");
        }
        return requestProcessor.getCandleSet(ticker, interval, start, end);
    }
}
