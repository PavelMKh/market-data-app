package com.pavelkhomenko.marketdata.repository;

import com.pavelkhomenko.marketdata.dto.Candle;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface CandleMongoRepository extends MongoRepository<Candle, String> {
    @Query("{'startDateTime': {$gt: ?0, $lt: ?1}, 'ticker':  ?2, 'interval': ?3}")
    List<Candle> getCandlesBetweenDates(Date start, Date end, String ticker, int interval, Sort sort);

    @Query(value = "{ 'ticker': { $exists: true }, 'source' : 'MOEX'}", fields = "{ '_id': 0, ticker: 1 }")
    Set<String> findDistinctByTickerMoex();

    @Query(value = "{ 'ticker' : { $exists: true }, 'source' : 'AlphaVantage'}", fields = "{ '_id': 0, ticker: 1 }")
    List<String> findDistinctByTickerAlphaVantage();

    @Query(value = "{ 'ticker': ?0, 'interval': ?1 }", sort = "{ 'startDateTime': -1 }",
            fields = "{ '_id': 0, 'startDateTime': 1 }")
    List<String> getLastDateForTicker(String ticker, int interval);
}
