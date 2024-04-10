package com.pavelkhomenko.marketdata.controller;

import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.service.CandleHistoryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebController {
    private final CandleHistoryService candleHistoryService;

    @GetMapping
    public String home() {
        return "index";
    }

    @GetMapping("/moex/history")
    public String showMoexCandlesHistoryForm(Model model) {
        model.addAttribute("ticker", "SBER");
        model.addAttribute("candlesize", 60);
        model.addAttribute("start", LocalDate.now().minusWeeks(1));
        model.addAttribute("end", LocalDate.now());
        return "moex-history-form";
    }


    @GetMapping("/moex/history/new")
    public String getCandlesHistoryMoexWeb(Model model,
                                           @RequestParam("ticker") String ticker,
                                           @RequestParam("candlesize") int interval,
                                           @RequestParam("startdate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate start,
                                           @RequestParam("enddate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate end) {
        List<Candle> candles = candleHistoryService.getMoexCandles(ticker, interval, start, end);
        model.addAttribute("ticker", ticker);
        model.addAttribute("candles", candles);
        return "history-result";
    }

    @GetMapping("/global/history/new")
    public String getCandlesHistoryGlobalWeb(Model model,
                                           @RequestParam("ticker") String ticker,
                                           @RequestParam("candlesize") int interval,
                                           @RequestParam("startdate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate start,
                                           @RequestParam("enddate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate end,
                                                   @RequestParam(name = "apikey") @NonNull String apiKey) {
        List<Candle> candles = candleHistoryService.getAlphaVantageCandles(ticker, interval, apiKey, start, end);
        model.addAttribute("ticker", ticker);
        model.addAttribute("candles", candles);
        return "history-result";
    }

    @GetMapping("/global/history")
    public String showGlobalCandlesHistoryForm(Model model) {
        model.addAttribute("ticker", "IBM");
        model.addAttribute("candlesize", 60);
        model.addAttribute("start", LocalDate.now().minusWeeks(1));
        model.addAttribute("end", LocalDate.now());
        return "global-history-form";
    }

    @GetMapping("/repo/history/new")
    public String getCandlesHistoryRepoWeb(Model model,
                                             @RequestParam("ticker") String ticker,
                                             @RequestParam("candlesize") int interval,
                                             @RequestParam("startdate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate start,
                                             @RequestParam("enddate") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate end) {
        List<Candle> candles = candleHistoryService.getCandlesFromDatabase(ticker, interval, start.toString(), end.toString());
        model.addAttribute("ticker", ticker);
        model.addAttribute("candles", candles);
        return "history-result";
    }

    @GetMapping("/repo/history")
    public String showRepoCandlesHistoryForm(Model model) {
        model.addAttribute("ticker", "SBER");
        model.addAttribute("candlesize", 60);
        model.addAttribute("start", LocalDate.now().minusWeeks(1));
        model.addAttribute("end", LocalDate.now());
        return "repo-history-form";
    }
}