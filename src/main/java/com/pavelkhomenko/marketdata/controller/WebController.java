package com.pavelkhomenko.marketdata.controller;

import com.pavelkhomenko.marketdata.entity.Candle;
import com.pavelkhomenko.marketdata.service.CandleHistoryService;
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
    public String showCandlesHistoryForm(Model model) {
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
        return "moex-history";
    }
}
