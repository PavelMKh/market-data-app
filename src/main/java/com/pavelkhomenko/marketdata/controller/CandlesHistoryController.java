package com.pavelkhomenko.marketdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.service.CandleHistoryService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class CandlesHistoryController {
    private final CandleHistoryService candleHistoryService;

    @GetMapping("/moex/shares/{ticker}/history")
    public ResponseEntity<List<Candle>> getCandlesHistoryMoex(@RequestParam("candlesize") int interval,
                                           @RequestParam("startdate") LocalDate start,
                                           @RequestParam("enddate") LocalDate end,
                                           @PathVariable String ticker) {
        return ResponseEntity.ok()
                .body(candleHistoryService.getMoexCandles(ticker, interval, start, end));
    }

    @GetMapping("/global/shares/{ticker}/history")
    public ResponseEntity<List<Candle>> getCandlesHistoryAlphaVantage(@RequestParam("candlesize") int interval,
                                          @RequestParam("startdate") LocalDate startDate,
                                          @RequestParam("enddate") LocalDate endDate,
                                          @RequestParam("apikey") String apikey,
                                          @PathVariable @NotBlank @NotEmpty String ticker) {
        return ResponseEntity.ok()
                .body(candleHistoryService.getAlphaVantageCandles(ticker, interval, apikey, startDate, endDate));
    }
    @GetMapping("/repo/shares/{ticker}/history")
    public ResponseEntity<List<Candle>> getCandlesHistoryFromRepository(@RequestParam("candlesize") int interval,
                                                        @RequestParam("startdate") LocalDate startDate,
                                                        @RequestParam("enddate") LocalDate endDate,
                                                        @PathVariable @NotBlank @NotEmpty String ticker) {
        return ResponseEntity.ok()
                .body(candleHistoryService.getCandlesFromDatabase(ticker, interval, startDate, endDate));
    }

    @GetMapping("/repo/reload/moex")
    public ResponseEntity<List<Candle>> reloadRepositoryMoex(
            @RequestParam("defaultStartDate") LocalDate defaultStartDate)
            throws JsonProcessingException {
        return ResponseEntity.ok()
                .body(candleHistoryService.reloadRepositoryMoex(defaultStartDate));
    }

    @GetMapping("/moex/shares/{ticker}/export-to-csv")
    public ResponseEntity<InputStreamResource>
                getCandlesHistoryMoexCsv(@RequestParam("candlesize") int interval,
                                         @RequestParam("startdate") LocalDate start,
                                         @RequestParam("enddate") LocalDate end,
                                         @PathVariable String ticker) {
        String fileName = String.format("%s_%s_%s_%s.csv", ticker, interval, start, end);
        InputStreamResource file = new InputStreamResource(candleHistoryService.loadFromMoexToCsv(ticker,
                interval, start, end));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @GetMapping("/global/shares/{ticker}/export-to-csv")
    public ResponseEntity<InputStreamResource>
                getCandlesHistoryAlphaVantageCsv(@RequestParam("candlesize") int interval,
                                                 @RequestParam("startdate") LocalDate startDate,
                                                 @RequestParam("enddate") LocalDate endDate,
                                                 @RequestParam("apikey") String apikey,
                                                 @PathVariable @NotBlank @NotEmpty String ticker) {
        String fileName = String.format("%s_%s_%s_%s.csv", ticker, interval, startDate, endDate);
        InputStreamResource file = new InputStreamResource(candleHistoryService.loadFromAlphaVantageToCsv(ticker,
                interval, apikey, startDate, endDate));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
    @GetMapping("/repo/shares/{ticker}/export-to-csv")
    public ResponseEntity<InputStreamResource>
                getCandlesHistoryFromRepositoryCsv(@RequestParam("candlesize") int interval,
                                                   @RequestParam("startdate") LocalDate startDate,
                                                   @RequestParam("enddate") LocalDate endDate,
                                                   @PathVariable @NotBlank @NotEmpty String ticker) {
        String fileName = String.format("%s_%s_%s_%s.csv", ticker, interval, startDate, endDate);
        InputStreamResource file = new InputStreamResource(candleHistoryService.loadFromRepoToCsv(ticker,
                interval, startDate, endDate));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
