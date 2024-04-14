package com.pc.kilojoules.services;

import com.pc.kilojoules.models.JournalTotalsDTO;
import com.pc.kilojoules.models.TopTenDTO;

import java.time.LocalDate;
import java.util.List;

public interface StatisticService {

    JournalTotalsDTO calculateJournalTotalsByDate(LocalDate date);

    JournalTotalsDTO calculateJournalTotalsByPeriod(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByKiloJoules(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByKiloJoulesCount(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByProteins(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByProteinsCount(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByCarbohydrates(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByCarbohydratesCount(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByFiber(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByFiberCount(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByFat(LocalDate startDate, LocalDate endDate);

    List<TopTenDTO> getTop10ByFatCount(LocalDate startDate, LocalDate endDate);
}
