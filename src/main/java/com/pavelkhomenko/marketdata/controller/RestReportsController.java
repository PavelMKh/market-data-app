package com.pavelkhomenko.marketdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.CompanyDataDto;
import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.entity.CashFlow;
import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import com.pavelkhomenko.marketdata.service.CompanyDataService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/reports")
public class RestReportsController {
    private final CompanyDataService companyDataService;

    @GetMapping("/{ticker}/overview")
    public Company getCompanyOverview(@PathVariable @NonNull String ticker,
                                      @RequestParam(name = "apikey") @NonNull String apiKey)
            throws JsonProcessingException {
        return companyDataService.getCompanyOverview(ticker, apiKey);
    }

    @GetMapping("/{ticker}/pnl")
    public List<IncomeStatement> getIncomeStatement(@PathVariable @NonNull String ticker,
                                                    @RequestParam(name = "apikey") @NonNull String apiKey)
            throws JsonProcessingException {
        return companyDataService.getIncomeStatement(ticker, apiKey);
    }

    @GetMapping("/{ticker}/bs")
    public List<BalanceSheet> getBalanceSheet(@PathVariable @NonNull String ticker,
                                              @RequestParam(name = "apikey") @NonNull String apiKey)
            throws JsonProcessingException {
        return companyDataService.getBalanceSheet(ticker, apiKey);
    }

    @GetMapping("/{ticker}/cf")
    public List<CashFlow> getCashFlow(@PathVariable @NonNull String ticker,
                                      @RequestParam(name = "apikey") @NonNull String apiKey)
            throws JsonProcessingException {
        return companyDataService.getCashFlow(ticker, apiKey);
    }

    @GetMapping("/{ticker}/all")
    public CompanyDataDto getAllReports(@PathVariable @NonNull String ticker,
                                              @RequestParam(name = "apikey") @NonNull String apiKey)
            throws JsonProcessingException {
        return companyDataService.getAllCompanyData(ticker, apiKey);
    }
}
