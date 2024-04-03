package com.pavelkhomenko.marketdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pavelkhomenko.marketdata.util.CustomLongDeserializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "balance_sheet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceSheet {
    @Id
    @JsonIgnore
    @Column(name = "id")
    private String id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "fiscaldateending")
    @JsonProperty("fiscalDateEnding")
    private LocalDate fiscalDateEnding;

    @Column(name = "type")
    private String type;

    @Column(name = "reportedcurrency")
    @JsonProperty("reportedCurrency")
    private String reportedCurrency;

    @Column(name = "totalassets")
    @JsonProperty("totalAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalAssets;

    @Column(name = "totalcurrentassets")
    @JsonProperty("totalCurrentAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalCurrentAssets;

    @Column(name = "cashandcashequivalentsatcarryingvalue")
    @JsonProperty("cashAndCashEquivalentsAtCarryingValue")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long cashAndCashEquivalentsAtCarryingValue;

    @Column(name = "cashandshortterminvestments")
    @JsonProperty("cashAndShortTermInvestments")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long cashAndShortTermInvestments;

    @Column(name = "inventory")
    @JsonProperty("inventory")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long inventory;

    @Column(name = "currentnetreceivables")
    @JsonProperty("currentNetReceivables")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long currentNetReceivables;

    @Column(name = "totalnoncurrentassets")
    @JsonProperty("totalNonCurrentAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalNonCurrentAssets;

    @Column(name = "propertyplantequipment")
    @JsonProperty("propertyPlantEquipment")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long propertyPlantEquipment;

    @Column(name = "accumulateddepreciationamortizationppe")
    @JsonProperty("accumulatedDepreciationAmortizationPPE")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long accumulatedDepreciationAmortizationPPE;

    @Column(name = "intangibleassets")
    @JsonProperty("intangibleAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long intangibleAssets;

    @Column(name = "intangibleassetsexcludinggoodwill")
    @JsonProperty("intangibleAssetsExcludingGoodwill")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long intangibleAssetsExcludingGoodwill;

    @Column(name = "goodwill")
    @JsonProperty("goodwill")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long goodwill;

    @Column(name = "investments")
    @JsonProperty("investments")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long investments;

    @Column(name = "longterminvestments")
    @JsonProperty("longTermInvestments")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long longTermInvestments;

    @Column(name = "shortterminvestments")
    @JsonProperty("shortTermInvestments")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long shortTermInvestments;

    @Column(name = "othercurrentassets")
    @JsonProperty("otherCurrentAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long otherCurrentAssets;

    @Column(name = "othernoncurrentassets")
    @JsonProperty("otherNonCurrentAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long otherNonCurrentAssets;

    @Column(name = "totalliabilities")
    @JsonProperty("totalLiabilities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalLiabilities;

    @Column(name = "totalcurrentliabilities")
    @JsonProperty("totalCurrentLiabilities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalCurrentLiabilities;

    @Column(name = "currentaccountspayable")
    @JsonProperty("currentAccountsPayable")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long currentAccountsPayable;

    @Column(name = "deferredrevenue")
    @JsonProperty("deferredRevenue")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long deferredRevenue;

    @Column(name = "currentdebt")
    @JsonProperty("currentDebt")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long currentDebt;

    @Column(name = "shorttermdebt")
    @JsonProperty("shortTermDebt")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long shortTermDebt;

    @Column(name = "totalnoncurrentliabilities")
    @JsonProperty("totalNonCurrentLiabilities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalNonCurrentLiabilities;

    @Column(name = "capitalleaseobligations")
    @JsonProperty("capitalLeaseObligations")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long capitalLeaseObligations;

    @Column(name = "longtermdebt")
    @JsonProperty("longTermDebt")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long longTermDebt;

    @Column(name = "currentlongtermdebt")
    @JsonProperty("currentLongTermDebt")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long currentLongTermDebt;

    @Column(name = "longtermdebtnoncurrent")
    @JsonProperty("longTermDebtNoncurrent")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long longTermDebtNonCurrent;

    @Column(name = "shortlongtermdettotal")
    @JsonProperty("shortLongTermDebtTotal")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long shortLongTermDebtTotal;

    @Column(name = "othercurrentliabilities")
    @JsonProperty("otherCurrentLiabilities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long otherCurrentLiabilities;

    @Column(name = "othernoncurrentliabilities")
    @JsonProperty("otherNonCurrentLiabilities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long otherNonCurrentLiabilities;

    @Column(name = "totalshareholderequity")
    @JsonProperty("totalShareholderEquity")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long totalShareholderEquity;

    @Column(name = "treasurystock")
    @JsonProperty("treasuryStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long treasuryStock;

    @Column(name = "retainedearnings")
    @JsonProperty("retainedEarnings")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long retainedEarnings;

    @Column(name = "commonstock")
    @JsonProperty("commonStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long commonStock;

    @Column(name = "commonstocksharesoutstanding")
    @JsonProperty("commonStockSharesOutstanding")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long commonStockSharesOutstanding;
}
