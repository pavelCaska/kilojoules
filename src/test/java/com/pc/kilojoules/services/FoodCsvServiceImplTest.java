package com.pc.kilojoules.services;

import com.pc.kilojoules.models.FoodCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FoodCsvServiceImplTest {

    FoodCsvService foodCsvService = new FoodCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/potraviny.csv");

        List<FoodCSVRecord> recs = foodCsvService.convertCSV(file);

        System.out.println(recs.size());

        assertThat(recs.size()).isGreaterThan(0);
    }
}