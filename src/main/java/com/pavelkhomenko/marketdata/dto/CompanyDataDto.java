package com.pavelkhomenko.marketdata.dto;

import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.entity.CashFlow;
import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDataDto {
    private Company company;
    private List<BalanceSheet> balanceSheet;
    private List<IncomeStatement> incomeStatement;
    private List<CashFlow> cashFlow;
}
