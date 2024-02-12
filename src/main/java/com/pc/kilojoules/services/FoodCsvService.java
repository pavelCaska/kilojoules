package com.pc.kilojoules.services;

import com.pc.kilojoules.models.FoodCSVRecord;

import java.io.File;
import java.util.List;

public interface FoodCsvService {
        List<FoodCSVRecord> convertCSV(File csvFile);

    }
