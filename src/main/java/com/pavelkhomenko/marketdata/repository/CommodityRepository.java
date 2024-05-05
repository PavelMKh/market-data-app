package com.pavelkhomenko.marketdata.repository;

import com.pavelkhomenko.marketdata.entity.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, String> {
}