package com.pavelkhomenko.marketdata.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.Constants;
import com.pavelkhomenko.marketdata.entity.Commodity;
import com.pavelkhomenko.marketdata.exceptions.IncorrectCandleSizeException;
import com.pavelkhomenko.marketdata.mapping.commodities.CommodityPriceMapping;
import com.pavelkhomenko.marketdata.repository.CommodityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommodityService {
    private final CommodityPriceMapping commodityPriceMapping;
    private final CommodityRepository commodityRepository;
    public List<Commodity> getCommodityHistory(String commodity, String interval, String apikey) throws JsonProcessingException {
        if (!Constants.COMMODITY_ALLOWED_INTERVAL.contains(interval)) {
            throw new IncorrectCandleSizeException("The interval can only take values: daily," +
                    "weekly, monthly");
        }
        List<Commodity> commodities = commodityPriceMapping.getCommodityPrices(commodity, interval, apikey);
        commodityRepository.saveAll(commodities);
        return commodities;
    }
}
