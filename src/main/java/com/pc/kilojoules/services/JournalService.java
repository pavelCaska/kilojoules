package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.*;
import com.pc.kilojoules.models.JournalEntryDTO;
import com.pc.kilojoules.models.JournalMealFormDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface JournalService {

    @Transactional
    Journal addFoodToJournal(Food food, BigDecimal quantity, LocalDate date, String mealType, String foodName);

    @Transactional
    Journal updateJournalFood(Long journalId, BigDecimal quantity, LocalDate date, String mealType, String foodName);

    Journal createJournalEntryWithFood(LocalDate date, String mealType, JournalFood jf);

    Journal createJournalEntryWithMeal(LocalDate date, String mealType, JournalMeal jm);

    @Transactional
    Journal updateJournalWithMeal(Long journalId, JournalMealFormDTO formDTO);

    List<Journal> getAllJournalEntries(LocalDate date);

    List<JournalEntryDTO> getAllJournalEntriesByDateAndMealType(LocalDate date, MealType mealType);

    List<JournalEntryDTO> convertJournalListToDTO(List<Journal> entries);

    Journal getJournalEntryById(Long id);

    boolean existsJournalByIdAndJournalFoodId(Long journalId, Long journalFoodId);

    boolean existsJournalByIdAndJournalMealId(Long journalId, Long journalMealId);

    Journal deleteJournal(Long id);

    Journal createJournalAndJournalMeal(Long id, JournalMealFormDTO formDTO);

    boolean doAllEntitiesExist(Long journalId, Long mealId, Long foodId);

    @Transactional
    String updateJournalMealWithJmf(Long journalId, Long foodId, BigDecimal quantity, BigDecimal portionSize, String foodName);

    @Transactional
    String addFoodToJournalMeal(Long journalId, Long foodId, BigDecimal quantity, BigDecimal portionSize);

    @Transactional
    String deleteJmfByIdAndJournalId(Long id, Long journalId);
}
