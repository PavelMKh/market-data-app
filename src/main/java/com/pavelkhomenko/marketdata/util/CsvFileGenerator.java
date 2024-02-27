package com.pavelkhomenko.marketdata.util;

import com.pavelkhomenko.marketdata.entity.Candle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class CsvFileGenerator {
    public void writeCandlesToCsv(List<Candle> candles, String ticker, LocalDate start,
                                  LocalDate end, int interval) {
        try {
            String fileName = String.format("%s_%s_%s_%s.csv", ticker, interval, start, end);
            Writer writer = new FileWriter(fileName);
            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (Candle candle : candles) {
                printer.printRecord(candle.getStartDateTime(),
                        candle.getTicker(),
                        candle.getInterval(),
                        candle.getOpen(),
                        candle.getMax(),
                        candle.getMin(),
                        candle.getClose(),
                        candle.getVolume());
            }
        } catch (IOException e) {
            log.info("Error writing to csv file");
        }
    }
}
