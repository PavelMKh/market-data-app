package com.pavelkhomenko.marketdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "commodities")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commodity {
    @Id
    @Column(length = 20)
    private String id;
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private double value;
}