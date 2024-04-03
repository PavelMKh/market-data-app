package com.pavelkhomenko.marketdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "company_overview")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncomeStatement {
    @Id
    @JsonIgnore
    private String id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "type")
    private String type;

    @JsonProperty("fiscalDateEnding")
    @Column(name = "fiscaldateending")
    private LocalDate fiscalDateEnding;

    @JsonProperty("reportedCurrency")
    @Column(name = "reportedcurrency")
    private String reportedCurrency;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("grossProfit")
    @Column(name = "grossprofit")
    private long grossProfit;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("totalRevenue")
    @Column(name = "totalrevenue")
    private long totalRevenue;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("costOfRevenue")
    @Column(name = "costofrevenue")
    private long costOfRevenue;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("costofGoodsAndServicesSold")
    @Column(name = "costofgoodsandservicessold")
    private long costofGoodsAndServicesSold;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("operatingIncome")
    @Column(name = "operatingincome")
    private long operatingIncome;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("sellingGeneralAndAdministrative")
    @Column(name = "sellinggeneralandadministrative")
    private long sellingGeneralAndAdministrative;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("researchAndDevelopment")
    @Column(name = "researchanddevelopment")
    private long researchAndDevelopment;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("operatingExpenses")
    @Column(name = "operatingexpenses")
    private long operatingExpenses;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("investmentIncomeNet")
    @Column(name = "investmentincomenet")
    private long investmentIncomeNet;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("netInterestIncome")
    @Column(name = "netinterestincome")
    private long netInterestIncome;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("interestIncome")
    @Column(name = "interestincome")
    private long interestIncome;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("interestExpense")
    @Column(name = "interestexpense")
    private long interestExpense;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("nonInterestIncome")
    @Column(name = "noninterestincome")
    private long nonInterestIncome;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("otherNonOperatingIncome")
    @Column(name = "othernonoperatingincome")
    private long otherNonOperatingIncome;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("depreciation")
    @Column(name = "depreciation")
    private long depreciation;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("depreciationAndAmortization")
    @Column(name = "depreciationandamortization")
    private long depreciationAndAmortization;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("incomeBeforeTax")
    @Column(name = "incomebeforetax")
    private long incomeBeforeTax;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("incomeTaxExpense")
    @Column(name = "incometaxexpense")
    private long incomeTaxExpense;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("interestAndDebtExpense")
    @Column(name = "interestanddebtexpense")
    private long interestAndDebtExpense;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("netIncomeFromContinuingOperations")
    @Column(name = "netincomefromcontinuingoperations")
    private long netIncomeFromContinuingOperations;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("comprehensiveIncomeNetOfTax")
    @Column(name = "comprehensiveincomenetoftax")
    private long comprehensiveIncomeNetOfTax;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("ebit")
    @Column(name = "ebit")
    private long ebit;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("ebitda")
    @Column(name = "ebitda")
    private long ebitda;

    @JsonDeserialize(using = CustomLongDeserializer.class)
    @JsonProperty("netIncome")
    @Column(name = "netincome")
    private long netIncome;
}
