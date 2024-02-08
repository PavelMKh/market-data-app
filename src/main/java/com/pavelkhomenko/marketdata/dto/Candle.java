package com.pavelkhomenko.marketdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Validated
public class Candle {

    @NonNull
    private LocalDateTime startDateTime;
    private float open;
    private float max;
    private float min;
    private float close;
    private float volume;

}