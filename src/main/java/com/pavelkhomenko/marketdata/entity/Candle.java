package com.pavelkhomenko.marketdata.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Table(name = "candles_history")
@Entity
public class Candle {
    @JsonIgnore
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "start_date_time")
    private OffsetDateTime startDateTime; // TODO: поменять на совместимый с Postgres класс
    @Column(name = "ticker")
    private String ticker;
    @Column(name = "interval")
    private int interval;
    @JsonIgnore
    @Column(name = "data_source")
    private String source;
    @Column(name = "open_price")
    private float open;
    @Column(name = "max_price")
    private float max;
    @Column(name = "min_price")
    private float min;
    @Column(name = "close_price")
    private float close;
    @Column(name = "volume")
    private float volume;

}