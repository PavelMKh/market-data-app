package com.pavelkhomenko.marketdata.repository.dal.dao;

import java.util.List;

public interface AnalyticalReportsDao {
    List<String> getTickerForUploading();
}
