package com.pavelkhomenko.marketdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@Validated
@Document(collection = "stock_data")
public class Candle {
    @JsonIgnore
    @Id
    private String id;
    private Date startDateTime;
    private String ticker;
    private int interval;
    @JsonIgnore
    private String source;
    private float open;
    private float max;
    private float min;
    private float close;
    private float volume;

}