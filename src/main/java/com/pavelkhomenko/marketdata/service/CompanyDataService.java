package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.dto.CompanyDataDto;
import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.entity.CashFlow;
import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import com.pavelkhomenko.marketdata.mapping.reports.BalanceSheetProcessing;
import com.pavelkhomenko.marketdata.mapping.reports.CashFlowProcessing;
import com.pavelkhomenko.marketdata.mapping.reports.CompanyOverviewProcessing;
import com.pavelkhomenko.marketdata.mapping.reports.IncomeStatementProcessing;
import com.pavelkhomenko.marketdata.repository.dal.BalanceSheetDao;
import com.pavelkhomenko.marketdata.repository.dal.CashFlowDao;
import com.pavelkhomenko.marketdata.repository.dal.CompanyOverviewDao;
import com.pavelkhomenko.marketdata.repository.dal.IncomeStatementDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyDataService {
    private final CompanyOverviewProcessing companyOverviewProcessing;
    private final CompanyOverviewDao companyOverviewDao;
    private final IncomeStatementProcessing pnlProcessing;
    private final IncomeStatementDao pnlDao;
    private final BalanceSheetProcessing bsProcessing;
    private final BalanceSheetDao bsDao;
    private final CashFlowDao cashFlowDao;
    private final CashFlowProcessing cfProcessing;

    public Company getCompanyOverview(String ticker, String apiKey)
            throws JsonProcessingException {
        Optional<Company> company = companyOverviewDao.getCompanyOverview(ticker);
        if (company.isEmpty()) {
            log.info("Requesting " + ticker + " overview from AlphaVantage");
            Company companyFromAv = companyOverviewProcessing.getOverview(ticker, apiKey);
            companyOverviewDao.saveCompanyOverview(companyFromAv);
            return companyFromAv;
        }
        return company.get();
    }

    public List<IncomeStatement> getIncomeStatement(String ticker, String apiKey) throws JsonProcessingException {
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
}
