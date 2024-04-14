package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.JournalMeal;
import com.pc.kilojoules.entities.JournalMealFood;
import com.pc.kilojoules.entities.Meal;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;


public interface JournalMealService {

    @Transactional
    JournalMeal createJournalMeal(Long id, String mealName);

    @Transactional
    JournalMeal convertMealToUnsavedJournalMeal(Meal meal);

    JournalMeal getJournalMealById(Long id);

    @Transactional
    String addFoodToUnsavedJournalMeal(Long mealId, Long foodId, BigDecimal quantity, BigDecimal portionSize);

    void updateSetWithJournalMealFood(JournalMeal jm, JournalMealFood jmf);

    void addJmfToJournalMealTotals(JournalMeal jm, JournalMealFood jmf);

    void removeJmfAndSubstractFromJournalMealTotals(JournalMeal jm, JournalMealFood jmf);

    @Transactional
    String updateUnsavedJournalMealWithJmf(Long mealId, Long foodId, BigDecimal quantity, BigDecimal portionSize, String foodName);

    boolean doJournalMealAndJmfExist(Long mealId, Long foodId);

    void calculateAndSetTotalFieldsFromSet(JournalMeal journalMeal, Set<JournalMealFood> journalMealFoods);

    void saveJournalMeal(JournalMeal jm);

    //    Quartz Scheduler
    boolean existsUnsavedJournalMeals();

    @Transactional
    void deleteUnsavedJournalMeals();
}
