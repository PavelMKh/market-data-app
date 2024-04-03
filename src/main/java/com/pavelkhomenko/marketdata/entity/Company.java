package com.pavelkhomenko.marketdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company_overview")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {
    @Id
    @JsonProperty("Symbol")
    private String ticker;
    @Column(name = "name")
    @JsonProperty("Name")
    private String companyName;
    @Column(name = "description")
    @JsonProperty("Description")
    private String description;
    @Column(name = "country")
    @JsonProperty("Country")
    private String country;
    @Column(name = "sector")
    @JsonProperty("Sector")
    private String sector;
    @Column(name = "industry")
    @JsonProperty("Industry")
    private String industry;
    @Column(name = "address")
    @JsonProperty("Address")
    private String adress;
}
