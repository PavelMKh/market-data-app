package com.pavelkhomenko.marketdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.entity.CashFlow;
import com.pavelkhomenko.marketdata.entity.Company;
import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import com.pavelkhomenko.marketdata.repository.dal.dao.BalanceSheetDao;
import com.pavelkhomenko.marketdata.repository.dal.dao.CashFlowDao;
import com.pavelkhomenko.marketdata.repository.dal.dao.CompanyOverviewDao;
import com.pavelkhomenko.marketdata.repository.dal.dao.IncomeStatementDao;
import com.pavelkhomenko.marketdata.repository.dal.impl.BalanceSheetDaoImpl;
import com.pavelkhomenko.marketdata.repository.dal.impl.CashFlowDaoImpl;
import com.pavelkhomenko.marketdata.repository.dal.impl.CompanyOverviewDaoImpl;
import com.pavelkhomenko.marketdata.repository.dal.impl.IncomeStatementDaoImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
public class RepositoryTest {
    public static DataSource dataSource;
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static JdbcTemplate jdbcTemplate;
    public static IncomeStatementDao incomeStatementDao;
    public static CompanyOverviewDao companyOverviewDao;
    public static Company testCompany;
    public static BalanceSheetDao balanceSheetDao;
    public static CashFlowDao cashFlowDao;

    @BeforeAll
    public static void createJdbcTemplate() throws IOException {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("/schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        incomeStatementDao = new IncomeStatementDaoImpl(jdbcTemplate);
        balanceSheetDao = new BalanceSheetDaoImpl(jdbcTemplate);
        cashFlowDao = new CashFlowDaoImpl(jdbcTemplate);
        companyOverviewDao = new CompanyOverviewDaoImpl(jdbcTemplate);
        testCompany = objectMapper.readValue(
                new String(Files.readAllBytes(Paths.get("src/test/resources/overview.json"))),
                Company.class);
        testCompany.setTicker("AAPL");
        companyOverviewDao.saveCompanyOverview(testCompany);
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void getPnlTest() throws IOException {
        IncomeStatement testPnl = objectMapper.readValue(
                new String(Files.readAllBytes(Paths.get("src/test/resources/pnl.json"))),
                IncomeStatement.class);
        testPnl.setId("AAPL2023-09-30");
        List<IncomeStatement> testPnlList = Collections.singletonList(testPnl);
        incomeStatementDao.savePnl(testPnlList);
        List<IncomeStatement> testPnlDb = incomeStatementDao.getPnl("AAPL");
        assertEquals(testPnlList, testPnlDb);
    }

    @Test
    public void getBsTest() throws IOException {
        BalanceSheet testBs = objectMapper.readValue(
                new String(Files.readAllBytes(Paths.get("src/test/resources/bs.json"))),
                BalanceSheet.class);
        testBs.setId("AAPL2023-09-30");
        List<BalanceSheet> testBsList = Collections.singletonList(testBs);
        balanceSheetDao.saveBalanceSheet(testBsList);
        List<BalanceSheet> testBsDb = balanceSheetDao.getBalanceSheet("AAPL");
        assertEquals(testBsList, testBsDb);
    }

    @Test
    public void getCfTest() throws IOException {
        CashFlow testCf = objectMapper.readValue(
                new String(Files.readAllBytes(Paths.get("src/test/resources/cf.json"))),
                CashFlow.class);
        testCf.setId("AAPL2023-09-30");
        List<CashFlow> testCfList = Collections.singletonList(testCf);
        cashFlowDao.saveCf(testCfList);
        List<CashFlow> testCfDb = cashFlowDao.getCf("AAPL");
        assertEquals(testCfList, testCfDb);
    }

    @Test
    public void getPnlTickerNotExistTest() {
        assertTrue(companyOverviewDao.getCompanyOverview("IBM").isEmpty());
    }

    @Test
    public void getOverviewTest() {
        Company aaplDb = companyOverviewDao.getCompanyOverview("AAPL").get();
        assertEquals(testCompany, aaplDb);
    }

    @Test
    public void getOverviewTickerNotExistTest() {
        assertEquals(companyOverviewDao.getCompanyOverview("IBM"), Optional.empty());
    }
}
