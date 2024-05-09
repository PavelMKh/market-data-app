package com.pavelkhomenko.marketdata.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pavelkhomenko.marketdata.entity.Commodity;
import com.pavelkhomenko.marketdata.service.CommodityService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/commodity")
public class CommodityController {
    private final CommodityService commodityService;
    @GetMapping("/{commodity}/history")
    public ResponseEntity<List<Commodity>>
            getCommodityHistory(@PathVariable @NotBlank @NotEmpty String commodity,
                                @RequestParam("apikey") String apikey,
                                @RequestParam("interval") String interval) throws JsonProcessingException {
        return ResponseEntity.ok()
                .body(commodityService.getCommodityHistory(commodity, interval, apikey));
    }
}
