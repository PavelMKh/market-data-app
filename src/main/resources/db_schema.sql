CREATE TABLE IF NOT EXISTS candles_history (
                                      id VARCHAR PRIMARY KEY,
                                      start_date_time TIMESTAMP NOT NULL,
                                      ticker VARCHAR(10) NOT NULL,
                                      interval INTEGER NOT NULL,
                                      data_source VARCHAR (20) NOT NULL,
                                      open_price float4,
                                      max_price float4,
                                      min_price float4,
                                      close_price float4,
                                      volume BIGINT
);