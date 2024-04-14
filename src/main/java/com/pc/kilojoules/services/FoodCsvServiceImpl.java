package com.pc.kilojoules.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.pc.kilojoules.models.FoodCSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class FoodCsvServiceImpl implements FoodCsvService {
    @Override
    public List<FoodCSVRecord> convertCSV(File csvFile) {
        try {
            List<FoodCSVRecord> foodCSVRecords = new CsvToBeanBuilder<FoodCSVRecord>(new FileReader(csvFile))
                    .withType(FoodCSVRecord.class)
                    .withSeparator(';')  // Set the semicolon as the delimiter
                    .build().parse();
            return foodCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}