package com.pavelkhomenko.marketdata.repository.dal.impl;

import com.pavelkhomenko.marketdata.Constants;
import com.pavelkhomenko.marketdata.repository.dal.dao.AnalyticalReportsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnalyticalReportsDaoImpl implements AnalyticalReportsDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AnalyticalReportsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getTickerForUploading() {
        String query = new StringBuilder().append("select ticker from company_overview " +
                "except select ticker from income_statement " +
                "limit ").append((int) Math.floor(Constants.FREE_API_LIMITATION / 3)).toString();
        // due to limitation of free API and 3 reports for company
        return jdbcTemplate.queryForList(query, String.class);
    }
}
