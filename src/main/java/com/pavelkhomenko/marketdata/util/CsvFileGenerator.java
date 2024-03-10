package com.pavelkhomenko.marketdata.util;

import com.pavelkhomenko.marketdata.entity.Candle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CsvFileGenerator {
    public ByteArrayInputStream writeCandlesToCsv(List<Candle> candles) {
        CSVFormat format = CSVFormat.DEFAULT.withHeader
                ("DateTime", "Ticker", "CandleSize", "Open", "Max", "Min", "Close", "Volume");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try (ByteArrayOutputStream candlesOut =
                     new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(candlesOut);
             CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
            for (Candle candle : candles) {
                List<String> candleData = Arrays.asList(
                        candle.getStartDateTime().format(formatter),
                        candle.getTicker(),
                        String.valueOf(candle.getInterval()),
                        String.valueOf(candle.getOpen()),
                        String.valueOf(candle.getMax()),
                        String.valueOf(candle.getMin()),
                        String.valueOf(candle.getClose()),
                        String.valueOf(candle.getVolume()));
                csvPrinter.printRecord(candleData);
            }
            csvPrinter.flush();
            writer.flush();
            return new ByteArrayInputStream(candlesOut.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
