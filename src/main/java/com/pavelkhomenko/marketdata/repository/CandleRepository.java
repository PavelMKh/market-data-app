package com.pavelkhomenko.marketdata.repository;

import com.pavelkhomenko.marketdata.entity.Candle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface CandleRepository extends JpaRepository<Candle, String> {

    @Query(value = "select * " +
            "from candles_history " +
            "where start_date_time between :startdate and :enddate " +
            "and ticker = :ticker " +
            "and interval = :interval " +
            "order by start_date_time;",
    nativeQuery = true)
    List<Candle> getCandlesBetweenDates(@Param("startdate") OffsetDateTime startDate,
                                        @Param("enddate") OffsetDateTime endDate,
                                        @Param("ticker") String ticker,
                                        @Param("interval") int interval);

    @Query(value = "select cast(max(start_date_time) as date) " +
            "from candles_history " +
            "where ticker = :ticker " +
            "and interval = :interval",
    nativeQuery = true)
    LocalDate getLastDateForTicker(@Param("ticker") String ticker,
                                   @Param("interval") int interval);
    @Query(value = "select distinct(ticker) " +
            "from candles_history " +
            "where data_source = 'MOEX';",
    nativeQuery = true)
    Set<String> findDistinctByTickerMoex();
}
