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
@Table(name = "cash_flow")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashFlow {
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

    @Column(name = "operatingcashflow")
    @JsonProperty("operatingCashflow")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long operatingCashFlow;

    @Column(name = "paymentsforoperatingactivities")
    @JsonProperty("paymentsForOperatingActivities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long paymentsForOperatingActivities;

    @Column(name = "proceedsfromoperatingactivities")
    @JsonProperty("proceedsFromOperatingActivities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromOperatingActivities;

    @Column(name = "changeinoperatingliabilities")
    @JsonProperty("changeInOperatingLiabilities")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long changeInOperatingLiabilities;

    @Column(name = "changeinoperatingassets")
    @JsonProperty("changeInOperatingAssets")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long changeInOperatingAssets;

    @Column(name = "depreciationdepletionandamortization")
    @JsonProperty("depreciationDepletionAndAmortization")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long depreciationDepletionAndAmortization;

    @Column(name = "capitalexpenditures")
    @JsonProperty("capitalExpenditures")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long capitalExpenditures;

    @Column(name = "changeinreceivables")
    @JsonProperty("changeInReceivables")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long changeInReceivables;

    @Column(name = "changeininventory")
    @JsonProperty("changeInInventory")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long changeInInventory;

    @Column(name = "profitloss")
    @JsonProperty("profitLoss")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long profitLoss;

    @Column(name = "cashflowfrominvestment")
    @JsonProperty("cashflowFromInvestment")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long cashFlowFromInvestment;

    @Column(name = "cashflowfromfinancing")
    @JsonProperty("cashflowFromFinancing")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long cashFlowFromFinancing;

    @Column(name = "proceedsfromrepaymentsofshorttermdet")
    @JsonProperty("proceedsFromRepaymentsOfShortTermDebt")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromRepaymentsOfStd;

    @Column(name = "paymentsforrepurchaseofcommonstock")
    @JsonProperty("paymentsForRepurchaseOfCommonStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long paymentsForRepurchaseOfCommonStock;

    @Column(name = "paymentsforrepurchaseofequity")
    @JsonProperty("paymentsForRepurchaseOfEquity")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long paymentsForRepurchaseOfEquity;

    @Column(name = "paymentsforrepurchaseofpreferredstock")
    @JsonProperty("paymentsForRepurchaseOfPreferredStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long paymentsForRepurchaseOfPreferredStock;

    @Column(name = "dividendpayout")
    @JsonProperty("dividendPayout")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long dividendPayout;

    @Column(name = "dividendpayoutcommonstock")
    @JsonProperty("dividendPayoutCommonStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long dividendPayoutCommonStock;

    @Column(name = "dividendpayoutpreferredstock")
    @JsonProperty("dividendPayoutPreferredStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long dividendPayoutPreferredStock;

    @Column(name = "proceedsfromissuanceofcommonstock")
    @JsonProperty("proceedsFromIssuanceOfCommonStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromIssuanceOfCommonStock;

    @Column(name = "proceedsfromissuanceoflongtermdetandcapitalsecuritiesnet")
    @JsonProperty("proceedsFromIssuanceOfLongTermDebtAndCapitalSecuritiesNet")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromIssuanceOfLtdAndCsn;

    @Column(name = "proceedsfromissuanceofpreferredstock")
    @JsonProperty("proceedsFromIssuanceOfPreferredStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromIssuanceOfPreferredStock;

    @Column(name = "proceedsfromrepurchaseofequity")
    @JsonProperty("proceedsFromRepurchaseOfEquity")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromRepurchaseOfEquity;

    @Column(name = "proceedsfromsaleoftreasurystock")
    @JsonProperty("proceedsFromSaleOfTreasuryStock")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long proceedsFromSaleOfTreasuryStock;

    @Column(name = "changeincashandcashequivalents")
    @JsonProperty("changeInCashAndCashEquivalents")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long changeInCashAndCashEquivalents;

    @Column(name = "changeinexchangerate")
    @JsonProperty("changeInExchangeRate")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long changeInExchangeRate;

    @Column(name = "netincome")
    @JsonProperty("netIncome")
    @JsonDeserialize(using = CustomLongDeserializer.class)
    private Long netIncome;
}
