package com.pavelkhomenko.marketdata.repository.dal;

import com.pavelkhomenko.marketdata.entity.BalanceSheet;

import java.util.List;

public interface BalanceSheetDao {
    List<BalanceSheet> getBalanceSheet(String ticker);

    void saveBalanceSheet(List<BalanceSheet> balanceSheet);
}
