package com.pavelkhomenko.marketdata.repository.dal;

import com.pavelkhomenko.marketdata.entity.CashFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class CashFlowDaoImpl implements CashFlowDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CashFlowDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<CashFlow> getCf(String ticker) {
        String cfQuery = "select * from cash_flow " +
                "where ticker = ?";
        return jdbcTemplate.query(cfQuery, this::mapRowToCf, ticker);
    }

    @Override
    @Transactional
    public void saveCf(List<CashFlow> cfList) {
        String saveCfQuery = "insert into cash_flow (id, " +
                "ticker, " +
                "fiscaldateending, " +
                "type, " +
                "reportedcurrency, " +
                "operatingcashflow, " +
                "paymentsforoperatingactivities, " +
                "proceedsfromoperatingactivities, " +
                "changeinoperatingliabilities, " +
                "changeinoperatingassets, " +
                "depreciationdepletionandamortization, " +
                "capitalexpenditures, " +
                "changeinreceivables, " +
                "changeininventory, " +
                "profitloss, " +
                "cashflowfrominvestment, " +
                "cashflowfromfinancing, " +
                "proceedsfromrepaymentsofshorttermdet, " +
                "paymentsforrepurchaseofcommonstock, " +
                "paymentsforrepurchaseofequity, " +
                "paymentsforrepurchaseofpreferredstock, " +
                "dividendpayout, " +
                "dividendpayoutcommonstock, " +
                "dividendpayoutpreferredstock, " +
                "proceedsfromissuanceofcommonstock, " +
                "proceedsfromissuanceoflongtermdetandcapitalsecuritiesnet, " +
                "proceedsfromissuanceofpreferredstock, " +
                "proceedsfromrepurchaseofequity, " +
                "proceedsfromsaleoftreasurystock, " +
                "changeincashandcashequivalents, " +
                "changeinexchangerate, " +
                "netincome) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(saveCfQuery, cfList, 100, (ps, cashFlow) -> {
            ps.setString(1, cashFlow.getId());
            ps.setString(2, cashFlow.getTicker());
            ps.setDate(3, Date.valueOf(cashFlow.getFiscalDateEnding()));
            ps.setString(4, cashFlow.getType());
            ps.setString(5, cashFlow.getReportedCurrency());
            ps.setLong(6, cashFlow.getOperatingCashFlow());
            ps.setLong(7, cashFlow.getPaymentsForOperatingActivities());
            ps.setLong(8, cashFlow.getProceedsFromOperatingActivities());
            ps.setLong(9, cashFlow.getChangeInOperatingLiabilities());
            ps.setLong(10, cashFlow.getChangeInOperatingAssets());
            ps.setLong(11, cashFlow.getDepreciationDepletionAndAmortization());
            ps.setLong(12, cashFlow.getCapitalExpenditures());
            ps.setLong(13, cashFlow.getChangeInReceivables());
            ps.setLong(14, cashFlow.getChangeInInventory());
            ps.setLong(15, cashFlow.getProfitLoss());
            ps.setLong(16, cashFlow.getCashFlowFromInvestment());
            ps.setLong(17, cashFlow.getCashFlowFromFinancing());
            ps.setLong(18, cashFlow.getProceedsFromRepaymentsOfStd());
            ps.setLong(19, cashFlow.getPaymentsForRepurchaseOfCommonStock());
            ps.setLong(20, cashFlow.getPaymentsForRepurchaseOfEquity());
            ps.setLong(21, cashFlow.getPaymentsForRepurchaseOfPreferredStock());
            ps.setLong(22, cashFlow.getDividendPayout());
            ps.setLong(23, cashFlow.getDividendPayoutCommonStock());
            ps.setLong(24, cashFlow.getDividendPayoutPreferredStock());
            ps.setLong(25, cashFlow.getProceedsFromIssuanceOfCommonStock());
            ps.setLong(26, cashFlow.getProceedsFromIssuanceOfLtdAndCsn());
            ps.setLong(27, cashFlow.getProceedsFromIssuanceOfPreferredStock());
            ps.setLong(28, cashFlow.getProceedsFromRepurchaseOfEquity());
            ps.setLong(29, cashFlow.getProceedsFromSaleOfTreasuryStock());
            ps.setLong(30, cashFlow.getChangeInCashAndCashEquivalents());
            ps.setLong(31, cashFlow.getChangeInExchangeRate());
            ps.setLong(32, cashFlow.getNetIncome());
        });
    }

    private CashFlow mapRowToCf(ResultSet resultSet, int rowNum) throws SQLException {
        return CashFlow.builder()
                .id(resultSet.getString("id"))
                .ticker(resultSet.getString("ticker"))
                .type(resultSet.getString("type"))
                .fiscalDateEnding(resultSet.getDate("fiscaldateending").toLocalDate())
                .reportedCurrency(resultSet.getString("reportedcurrency"))
                .operatingCashFlow(resultSet.getLong("operatingcashflow"))
                .paymentsForOperatingActivities(resultSet.getLong("paymentsforoperatingactivities"))
                .proceedsFromOperatingActivities(resultSet.getLong("proceedsfromoperatingactivities"))
                .changeInOperatingLiabilities(resultSet.getLong("changeinoperatingliabilities"))
                .changeInOperatingAssets(resultSet.getLong("changeinoperatingassets"))
                .depreciationDepletionAndAmortization(resultSet.getLong("depreciationdepletionandamortization"))
                .capitalExpenditures(resultSet.getLong("capitalexpenditures"))
                .changeInReceivables(resultSet.getLong("changeinreceivables"))
                .changeInInventory(resultSet.getLong("changeininventory"))
                .profitLoss(resultSet.getLong("profitloss"))
                .cashFlowFromInvestment(resultSet.getLong("cashflowfrominvestment"))
                .cashFlowFromFinancing(resultSet.getLong("cashflowfromfinancing"))
                .proceedsFromRepaymentsOfStd(resultSet.getLong("proceedsfromrepaymentsofshorttermdet"))
                .paymentsForRepurchaseOfCommonStock(resultSet.getLong("paymentsforrepurchaseofcommonstock"))
                .paymentsForRepurchaseOfEquity(resultSet.getLong("paymentsforrepurchaseofequity"))
                .paymentsForRepurchaseOfPreferredStock(resultSet.getLong("paymentsforrepurchaseofpreferredstock"))
                .dividendPayout(resultSet.getLong("dividendpayout"))
                .dividendPayoutCommonStock(resultSet.getLong("dividendpayoutcommonstock"))
                .dividendPayoutPreferredStock(resultSet.getLong("dividendpayoutpreferredstock"))
                .proceedsFromIssuanceOfCommonStock(resultSet.getLong("proceedsfromissuanceofcommonstock"))
                .proceedsFromIssuanceOfLtdAndCsn(resultSet.getLong("proceedsfromissuanceoflongtermdetandcapitalsecuritiesnet"))
                .proceedsFromIssuanceOfPreferredStock(resultSet.getLong("proceedsfromissuanceofpreferredstock"))
                .proceedsFromRepurchaseOfEquity(resultSet.getLong("proceedsfromrepurchaseofequity"))
                .proceedsFromSaleOfTreasuryStock(resultSet.getLong("proceedsfromsaleoftreasurystock"))
                .changeInCashAndCashEquivalents(resultSet.getLong("changeincashandcashequivalents"))
                .changeInExchangeRate(resultSet.getLong("changeinexchangerate"))
                .netIncome(resultSet.getLong("netincome"))
                .build();
    }
}
