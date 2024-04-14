package com.pc.kilojoules.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class FoodCSVRecord {

    @CsvBindByName(column = "Název")
    private String name;
    @CsvBindByName(column = "Čas zápisu")
    @CsvDate("dd.MM.yyyy")  // Specify the date format
    private Date createdAt;
    @CsvBindByName(column = "Množství")
    @CsvNumber("#,#")  // Use a pattern that supports commas as decimal separators
    private BigDecimal quantity;
    @CsvBindByName(column = "kJ")
    @CsvNumber("#,#")
    private BigDecimal kiloJoules;
    @CsvBindByName(column = "Bílkoviny [g]")
    @CsvNumber("#,#")
    private BigDecimal proteins;
    @CsvBindByName(column = "Sacharidy [g]")
    @CsvNumber("#,#")
    private BigDecimal carbohydrates;
    @CsvBindByName(column = "Cukry [g]")
    @CsvNumber("#,#")
    private BigDecimal sugar;
    @CsvBindByName(column = "Tuky [g]")
    @CsvNumber("#,#")
    private BigDecimal fat;
    @CsvBindByName(column = "Nasycené mastné kyseliny [g]")
    @CsvNumber("#,#")
    private BigDecimal safa;
    @CsvBindByName(column = "Trans mastné kyseliny [g]")
    @CsvNumber("#,#")
    private BigDecimal tfa;
    @CsvBindByName(column = "Cholesterol [mg]")
    @CsvNumber("#,#")
    private BigDecimal cholesterol;
    @CsvBindByName(column = "Vláknina [g]")
    @CsvNumber("#,#")
    private BigDecimal fiber;
    @CsvBindByName(column = "Sodík [mg]")
    @CsvNumber("#,#")
    private BigDecimal sodium;
    @CsvBindByName(column = "Vápník [g]")
    @CsvNumber("#,#")
    private BigDecimal calcium;
    @CsvBindByName(column = "PHE [mg]")
    @CsvNumber("#,#")
    private BigDecimal phe;

}
