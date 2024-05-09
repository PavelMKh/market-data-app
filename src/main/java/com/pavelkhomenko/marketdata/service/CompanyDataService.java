package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.CompanyDataDto;
import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.entity.CashFlow;
import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import com.pavelkhomenko.marketdata.exceptions.ProcessingException;
import com.pavelkhomenko.marketdata.mapping.reports.BalanceSheetMapping;
import com.pavelkhomenko.marketdata.mapping.reports.CashFlowMapping;
import com.pavelkhomenko.marketdata.mapping.reports.CompanyOverviewMapping;
import com.pavelkhomenko.marketdata.mapping.reports.IncomeStatementMapping;
import com.pavelkhomenko.marketdata.repository.dal.dao.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyDataService {
    private final CompanyOverviewMapping companyOverviewMapping;
    private final CompanyOverviewDao companyOverviewDao;
    private final IncomeStatementMapping pnlProcessing;
    private final IncomeStatementDao pnlDao;
    private final BalanceSheetMapping bsProcessing;
    private final BalanceSheetDao bsDao;
    private final CashFlowDao cashFlowDao;
    private final CashFlowMapping cfProcessing;
    private final AnalyticalReportsDao analyticalReportsDao;

    public Company getCompanyOverview(String ticker, String apiKey)
            throws JsonProcessingException {
        log.info("Requesting " + ticker + "overview from database");
        Optional<Company> company = companyOverviewDao.getCompanyOverview(ticker);
        if (company.isEmpty()) {
            log.info("Requesting " + ticker + " overview from AlphaVantage");
            Company companyFromAv = companyOverviewMapping.getOverview(ticker, apiKey);
            companyOverviewDao.saveCompanyOverview(companyFromAv);
            return companyFromAv;
        }
        return company.get();
    }

    public List<IncomeStatement> getIncomeStatement(String ticker, String apiKey) throws JsonProcessingException {
        log.info("Requesting " + ticker + "income statement from database");
        List<IncomeStatement> incomeStatements = pnlDao.getPnl(ticker);
        if (incomeStatements.isEmpty()) {
            log.info(ticker + " income statement not found in the database");
            log.info("Requesting " + ticker + " income statement from AlphaVantage");
            incomeStatements = pnlProcessing.getPnlList(ticker, apiKey);
            pnlDao.savePnl(incomeStatements);
        }
        return incomeStatements;
    }

    public List<BalanceSheet> getBalanceSheet(String ticker, String apiKey) throws JsonProcessingException {
        log.info("Requesting " + ticker + "balance sheet from database");
        List<BalanceSheet> balanceSheets = bsDao.getBalanceSheet(ticker);
        if (balanceSheets.isEmpty()) {
            log.info(ticker + " balance sheet not found in the database");
            log.info("Requesting " + ticker + " balance sheet from AlphaVantage");
            balanceSheets = bsProcessing.getBsList(ticker, apiKey);
            bsDao.saveBalanceSheet(balanceSheets);
        }
        return balanceSheets;
    }

    public List<CashFlow> getCashFlow(String ticker, String apiKey) throws JsonProcessingException {
        log.info("Requesting " + ticker + "cash flow from database");
        List<CashFlow> cashFlow = cashFlowDao.getCf(ticker);
        if (cashFlow.isEmpty()) {
            log.info(ticker + " cash flow not found in the database");
            log.info("Requesting " + ticker + " cash flow from AlphaVantage");
            cashFlow = cfProcessing.getCfList(ticker, apiKey);
            cashFlowDao.saveCf(cashFlow);
        }
        return cashFlow;
    }

    public CompanyDataDto getAllCompanyData(String ticker, String apiKey) throws JsonProcessingException {
        return CompanyDataDto.builder()
                .company(getCompanyOverview(ticker, apiKey))
                .incomeStatement(getIncomeStatement(ticker, apiKey))
                .balanceSheet(getBalanceSheet(ticker, apiKey))
                .cashFlow(getCashFlow(ticker, apiKey))
                .build();
    }

    public List<String> uploadData(String apiKey) {
        List<String> tickersForUploading = analyticalReportsDao.getTickerForUploading();
        uploadBs(tickersForUploading, apiKey);
        uploadCf(tickersForUploading, apiKey);
        uploadPnl(tickersForUploading, apiKey);
        return tickersForUploading;
    }

    private void uploadBs(List<String> tickersForUploading, String apiKey) {
        List<BalanceSheet> bsReports = tickersForUploading.stream()
                .parallel()
                .map(ticker -> {
                    try {
                        return bsProcessing.getBsList(ticker, apiKey);
                    } catch (JsonProcessingException e) {
                        throw new ProcessingException("Balance sheet report processing error");
                    }
                })
                .flatMap(Collection::parallelStream)
                .toList();
        bsDao.saveBalanceSheet(bsReports);
    }

    private void uploadCf(List<String> tickersForUploading, String apiKey) {
        List<CashFlow> cfReports = tickersForUploading.stream()
                .parallel()
                .map(ticker -> {
                    try {
                        return cfProcessing.getCfList(ticker, apiKey);
                    } catch (JsonProcessingException e) {
                        throw new ProcessingException("Cash flow report processing error");
                    }
                })
                .flatMap(Collection::parallelStream)
                .toList();
        cashFlowDao.saveCf(cfReports);
    }

    private void uploadPnl(List<String> tickersForUploading, String apiKey) {
        List<IncomeStatement> pnlReports = tickersForUploading.stream()
                .parallel()
                .map(ticker -> {
                    try {
                        return pnlProcessing.getPnlList(ticker, apiKey);
                    } catch (JsonProcessingException e) {
                        throw new ProcessingException("Income statement report processing error");
                    }
                })
                .flatMap(Collection::parallelStream)
                .toList();
        pnlDao.savePnl(pnlReports);
    }
}
