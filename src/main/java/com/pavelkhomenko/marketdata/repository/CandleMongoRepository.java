package com.pavelkhomenko.marketdata.repository;

import com.pavelkhomenko.marketdata.dto.Candle;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public interface CandleMongoRepository extends MongoRepository<Candle, String> {
    @Query("{'startDateTime': {$gt: ?0, $lt: ?1}}")
    List<Candle> getCandlesBetweenDates(Date start, Date end, String ticker, int interval, Sort sort);
}
