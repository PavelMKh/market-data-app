package com.pavelkhomenko.marketdata.repository.dal.dao;

import com.pavelkhomenko.marketdata.entity.IncomeStatement;

import java.util.List;

public interface IncomeStatementDao {
    List<IncomeStatement> getPnl(String ticker);

    void savePnl(List<IncomeStatement> pnlList);
}
