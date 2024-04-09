package com.pavelkhomenko.marketdata.repository.dal.dao;

import com.pavelkhomenko.marketdata.entity.CashFlow;

import java.util.List;

public interface CashFlowDao {
    List<CashFlow> getCf(String ticker);

    void saveCf(List<CashFlow> cfList);
}
