package com.pavelkhomenko.marketdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class MarketdataApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketdataApplication.class, args);
	}

}
