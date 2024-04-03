package com.pavelkhomenko.marketdata.repository.dal;

import com.pavelkhomenko.marketdata.entity.IncomeStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class IncomeStatementDaoImpl implements IncomeStatementDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public IncomeStatementDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<IncomeStatement> getPnl(String ticker) {
        String getPnlQuery = "select * from income_statement " +
                "where ticker = ?";
        return jdbcTemplate.query(getPnlQuery, this::mapRowToPnl, ticker);
    }

    @Override
    @Transactional
    public void savePnl(List<IncomeStatement> pnlList) {
        String savePnlQuery = "insert into income_statement(id, ticker, type, " +
                "fiscaldateending, " +
                "reportedcurrency, " +
                "grossprofit, " +
                "totalrevenue, " +
                "costofrevenue, " +
                "costofgoodsandservicessold, " +
                "operatingincome, " +
                "sellinggeneralandadministrative, " +
                "researchanddevelopment, " +
                "operatingexpenses, " +
                "investmentincomenet," +
                "netinterestincome, " +
                "interestincome, " +
                "interestexpense, " +
                "noninterestincome, " +
                "othernonoperatingincome, " +
                "depreciation, " +
                "depreciationandamortization, " +
                "incomebeforetax, " +
                "incometaxexpense, " +
                "interestanddebtexpense," +
                " netincomefromcontinuingoperations, " +
                "comprehensiveincomenetoftax, " +
                "ebit, " +
                "ebitda, " +
                "netincome) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(savePnlQuery,
                pnlList,
                100,
                (PreparedStatement ps, IncomeStatement pnl) -> {
                    ps.setString(1, pnl.getId());
                    ps.setString(2, pnl.getTicker());
                    ps.setString(3, pnl.getType());
                    ps.setDate(4, Date.valueOf(pnl.getFiscalDateEnding()));
                    ps.setString(5, pnl.getReportedCurrency());
                    ps.setLong(6, pnl.getGrossProfit());
                    ps.setLong(7, pnl.getTotalRevenue());
                    ps.setLong(8, pnl.getCostOfRevenue());
                    ps.setLong(9, pnl.getCostofGoodsAndServicesSold());
                    ps.setLong(10, pnl.getOperatingIncome());
                    ps.setLong(11, pnl.getSellingGeneralAndAdministrative());
                    ps.setLong(12, pnl.getResearchAndDevelopment());
                    ps.setLong(13, pnl.getOperatingExpenses());
                    ps.setLong(14, pnl.getInvestmentIncomeNet());
                    ps.setLong(15, pnl.getNetInterestIncome());
                    ps.setLong(16, pnl.getInterestIncome());
                    ps.setLong(17, pnl.getInterestExpense());
                    ps.setLong(18, pnl.getNonInterestIncome());
                    ps.setLong(19, pnl.getOtherNonOperatingIncome());
                    ps.setLong(20, pnl.getDepreciation());
                    ps.setLong(21, pnl.getDepreciationAndAmortization());
                    ps.setLong(22, pnl.getIncomeBeforeTax());
                    ps.setLong(23, pnl.getIncomeTaxExpense());
                    ps.setLong(24, pnl.getInterestAndDebtExpense());
                    ps.setLong(25, pnl.getNetIncomeFromContinuingOperations());
                    ps.setLong(26, pnl.getComprehensiveIncomeNetOfTax());
                    ps.setLong(27, pnl.getEbit());
                    ps.setLong(28, pnl.getEbitda());
                    ps.setLong(29, pnl.getNetIncome());
                }
        );
    }

    private IncomeStatement mapRowToPnl(ResultSet resultSet, int rowNum) throws SQLException {
        return IncomeStatement.builder()
                .id(resultSet.getString("id"))
                .ticker(resultSet.getString("ticker"))
                .type(resultSet.getString("type"))
                .fiscalDateEnding(resultSet.getDate("fiscaldateending").toLocalDate())
                .reportedCurrency(resultSet.getString("reportedcurrency"))
                .grossProfit(resultSet.getLong("grossprofit"))
                .totalRevenue(resultSet.getLong("totalrevenue"))
                .costOfRevenue(resultSet.getLong("costofrevenue"))
                .costofGoodsAndServicesSold(resultSet.getLong("costofgoodsandservicessold"))
                .operatingIncome(resultSet.getLong("operatingincome"))
                .sellingGeneralAndAdministrative(resultSet.getLong("sellinggeneralandadministrative"))
                .researchAndDevelopment(resultSet.getLong("researchanddevelopment"))
                .operatingExpenses(resultSet.getLong("operatingexpenses"))
                .investmentIncomeNet(resultSet.getLong("investmentincomenet"))
                .netInterestIncome(resultSet.getLong("netinterestincome"))
                .interestIncome(resultSet.getLong("interestincome"))
                .interestExpense(resultSet.getLong("interestexpense"))
                .nonInterestIncome(resultSet.getLong("noninterestincome"))
                .otherNonOperatingIncome(resultSet.getLong("othernonoperatingincome"))
                .depreciation(resultSet.getLong("depreciation"))
                .depreciationAndAmortization(resultSet.getLong("depreciationandamortization"))
                .incomeBeforeTax(resultSet.getLong("incomebeforetax"))
                .incomeTaxExpense(resultSet.getLong("incometaxexpense"))
                .interestAndDebtExpense(resultSet.getLong("interestanddebtexpense"))
                .netIncomeFromContinuingOperations(resultSet.getLong("netincomefromcontinuingoperations"))
                .comprehensiveIncomeNetOfTax(resultSet.getLong("comprehensiveincomenetoftax"))
                .ebit(resultSet.getLong("ebit"))
                .ebitda(resultSet.getLong("ebitda"))
                .netIncome(resultSet.getLong("netincome"))
                .build();
    }
}
