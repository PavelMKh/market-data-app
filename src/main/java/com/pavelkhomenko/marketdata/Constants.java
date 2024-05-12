package com.pavelkhomenko.marketdata;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    public static final Set<Integer> MOEX_CANDLE_SIZE = new HashSet<>(Set.of(1, 10, 60, 24, 7, 31, 4));
    public static final Set<Integer> ALPHA_VANTAGE_CANDLE_SIZE = new HashSet<>(Set.of(1, 5, 15, 30, 60, 24));
    // 24 - daily candles
    public static final Set<String> COMMODITY_ALLOWED_INTERVAL = new HashSet<>(Set.of("daily", "weekly", "monthly"));
    public static final DateTimeFormatter CANDLES_DATETIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    public static final int FREE_API_LIMITATION = 24;

    public static final Set<String> ALLOWED_COMMODITIES_NAME = new HashSet<>(Set.of("WTI", "BRENT",
            "NATURAL_GAS", "COOPER", "ALUMINUM", "WHEAT", "CORN", "COTTON",
            "SUGAR", "COFFEE", "ALL_COMMODITIES"));
}
