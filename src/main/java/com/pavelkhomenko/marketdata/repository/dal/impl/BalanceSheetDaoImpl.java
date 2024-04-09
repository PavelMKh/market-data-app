package com.pavelkhomenko.marketdata.repository.dal.impl;

import com.pavelkhomenko.marketdata.entity.BalanceSheet;
import com.pavelkhomenko.marketdata.repository.dal.dao.BalanceSheetDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BalanceSheetDaoImpl implements BalanceSheetDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BalanceSheetDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BalanceSheet> getBalanceSheet(String ticker) {
        String getBsQuery = "select * from balance_sheet where ticker=?";
        return jdbcTemplate.query(getBsQuery, this::mapRowToBs, ticker);
    }

    @Override
    @Transactional
    public void saveBalanceSheet(List<BalanceSheet> balanceSheet) {
        String saveBsQuery = "insert into balance_sheet (id, ticker, " +
                "fiscaldateending, " +
                "type, " +
                "reportedcurrency, " +
                "totalassets, " +
                "totalcurrentassets, " +
                "cashandcashequivalentsatcarryingvalue, " +
                "cashandshortterminvestments, " +
                "inventory, " +
                "currentnetreceivables, " +
                "totalnoncurrentassets, " +
                "propertyplantequipment, " +
                "accumulateddepreciationamortizationppe, " +
                "intangibleassets, " +
                "intangibleassetsexcludinggoodwill, " +
                "goodwill, " +
                "investments, " +
                "longterminvestments, " +
                "shortterminvestments, " +
                "othercurrentassets, " +
                "othernoncurrentassets, " +
                "totalliabilities, " +
                "totalcurrentliabilities, " +
                "currentaccountspayable, " +
                "deferredrevenue, " +
                "currentdebt, " +
                "shorttermdebt, " +
                "totalnoncurrentliabilities, " +
                "capitalleaseobligations, " +
                "longtermdebt, " +
                "currentlongtermdebt, " +
                "longtermdebtnoncurrent, " +
                "shortlongtermdettotal, " +
                "othercurrentliabilities, " +
                "othernoncurrentliabilities, " +
                "totalshareholderequity, " +
                "treasurystock, " +
                "retainedearnings, " +
                "commonstock, " +
                "commonstocksharesoutstanding) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(saveBsQuery,
                balanceSheet,
                100,
                (PreparedStatement ps, BalanceSheet bs) -> {
                    ps.setString(1, bs.getId());
                    ps.setString(2, bs.getTicker());
                    ps.setDate(3, java.sql.Date.valueOf(bs.getFiscalDateEnding()));
                    ps.setString(4, bs.getType());
                    ps.setString(5, bs.getReportedCurrency());
                    ps.setLong(6, bs.getTotalAssets());
                    ps.setLong(7, bs.getTotalCurrentAssets());
                    ps.setLong(8, bs.getCashAndCashEquivalentsAtCarryingValue());
                    ps.setLong(9, bs.getCashAndShortTermInvestments());
                    ps.setLong(10, bs.getInventory());
                    ps.setLong(11, bs.getCurrentNetReceivables());
                    ps.setLong(12, bs.getTotalNonCurrentAssets());
                    ps.setLong(13, bs.getPropertyPlantEquipment());
                    ps.setLong(14, bs.getAccumulatedDepreciationAmortizationPPE());
                    ps.setLong(15, bs.getIntangibleAssets());
                    ps.setLong(16, bs.getIntangibleAssetsExcludingGoodwill());
                    ps.setLong(17, bs.getGoodwill());
                    ps.setLong(18, bs.getInvestments());
                    ps.setLong(19, bs.getLongTermInvestments());
                    ps.setLong(20, bs.getShortTermInvestments());
                    ps.setLong(21, bs.getOtherCurrentAssets());
                    ps.setLong(22, bs.getOtherNonCurrentAssets());
                    ps.setLong(23, bs.getTotalLiabilities());
                    ps.setLong(24, bs.getTotalCurrentLiabilities());
                    ps.setLong(25, bs.getCurrentAccountsPayable());
                    ps.setLong(26, bs.getDeferredRevenue());
                    ps.setLong(27, bs.getCurrentDebt());
                    ps.setLong(28, bs.getShortTermDebt());
                    ps.setLong(29, bs.getTotalNonCurrentLiabilities());
                    ps.setLong(30, bs.getCapitalLeaseObligations());
                    ps.setLong(31, bs.getLongTermDebt());
                    ps.setLong(32, bs.getCurrentLongTermDebt());
                    ps.setLong(33, bs.getLongTermDebtNonCurrent());
                    ps.setLong(34, bs.getShortLongTermDebtTotal());
                    ps.setLong(35, bs.getOtherCurrentLiabilities());
                    ps.setLong(36, bs.getOtherNonCurrentLiabilities());
                    ps.setLong(37, bs.getTotalShareholderEquity());
                    ps.setLong(38, bs.getTreasuryStock());
                    ps.setLong(39, bs.getRetainedEarnings());
                    ps.setLong(40, bs.getCommonStock());
                    ps.setLong(41, bs.getCommonStockSharesOutstanding());
                });
    }

    private BalanceSheet mapRowToBs (ResultSet rs, int rowNum) throws SQLException {
        return BalanceSheet.builder()
                .id(rs.getString("id"))
                .ticker(rs.getString("ticker"))
                .fiscalDateEnding(rs.getDate("fiscaldateending").toLocalDate())
                .type(rs.getString("type"))
                .reportedCurrency(rs.getString("reportedcurrency"))
                .totalAssets(rs.getLong("totalassets"))
                .totalCurrentAssets(rs.getLong("totalcurrentassets"))
                .cashAndCashEquivalentsAtCarryingValue(rs.getLong("cashandcashequivalentsatcarryingvalue"))
                .cashAndShortTermInvestments(rs.getLong("cashandshortterminvestments"))
                .inventory(rs.getLong("inventory"))
                .currentNetReceivables(rs.getLong("currentnetreceivables"))
                .totalNonCurrentAssets(rs.getLong("totalnoncurrentassets"))
                .propertyPlantEquipment(rs.getLong("propertyplantequipment"))
                .accumulatedDepreciationAmortizationPPE(rs.getLong("accumulateddepreciationamortizationppe"))
                .intangibleAssets(rs.getLong("intangibleassets"))
                .intangibleAssetsExcludingGoodwill(rs.getLong("intangibleassetsexcludinggoodwill"))
                .goodwill(rs.getLong("goodwill"))
                .investments(rs.getLong("investments"))
                .longTermInvestments(rs.getLong("longterminvestments"))
                .shortTermInvestments(rs.getLong("shortterminvestments"))
                .otherCurrentAssets(rs.getLong("othercurrentassets"))
                .otherNonCurrentAssets(rs.getLong("othernoncurrentassets"))
                .totalLiabilities(rs.getLong("totalliabilities"))
                .totalCurrentLiabilities(rs.getLong("totalcurrentliabilities"))
                .currentAccountsPayable(rs.getLong("currentaccountspayable"))
                .deferredRevenue(rs.getLong("deferredrevenue"))
                .currentDebt(rs.getLong("currentdebt"))
                .shortTermDebt(rs.getLong("shorttermdebt"))
                .totalNonCurrentLiabilities(rs.getLong("totalnoncurrentliabilities"))
                .capitalLeaseObligations(rs.getLong("capitalleaseobligations"))
                .longTermDebt(rs.getLong("longtermdebt"))
                .currentLongTermDebt(rs.getLong("currentlongtermdebt"))
                .longTermDebtNonCurrent(rs.getLong("longtermdebtnoncurrent"))
                .shortLongTermDebtTotal(rs.getLong("shortlongtermdettotal"))
                .otherCurrentLiabilities(rs.getLong("othercurrentliabilities"))
                .otherNonCurrentLiabilities(rs.getLong("othernoncurrentliabilities"))
                .totalShareholderEquity(rs.getLong("totalshareholderequity"))
                .treasuryStock(rs.getLong("treasurystock"))
                .retainedEarnings(rs.getLong("retainedearnings"))
                .commonStock(rs.getLong("commonstock"))
                .commonStockSharesOutstanding(rs.getLong("commonstocksharesoutstanding"))
                .build();
    }
}
