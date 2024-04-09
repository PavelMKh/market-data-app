package com.pavelkhomenko.marketdata.repository.dal.impl;

import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.repository.dal.dao.CompanyOverviewDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
@Slf4j
public class CompanyOverviewDaoImpl implements CompanyOverviewDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyOverviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Company> getCompanyOverview(String ticker) {
        String getCompanyQuery = "select * from company_overview " +
                "where ticker = ?";
        Company company;
        try {
            company = jdbcTemplate.queryForObject(getCompanyQuery, this::mapRowToCompany, ticker);
        } catch(EmptyResultDataAccessException e) {
            log.info("Company " + ticker + " not found in the database");
            return Optional.empty();
        }
        return Optional.ofNullable(company);
    }

    @Override
    @Transactional
    public void saveCompanyOverview(Company company) {
        String saveQuery = "insert into company_overview(ticker, name, description," +
                "country, sector, industry, address) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(saveQuery,
                company.getTicker(),
                company.getCompanyName(),
                company.getDescription(),
                company.getCountry(),
                company.getSector(),
                company.getIndustry(),
                company.getAdress());
    }

    private Company mapRowToCompany(ResultSet resultSet, int rowNum) throws SQLException {
        return new Company(resultSet.getString("ticker"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("country"),
                resultSet.getString("sector"),
                resultSet.getString("industry"),
                resultSet.getString("address"));
    }
}
