package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.JournalMeal;
import com.pc.kilojoules.entities.JournalMealFood;

import java.math.BigDecimal;

public interface JournalMealFoodService {

    JournalMealFood saveJournalMealFood(JournalMealFood journalMealFood);

    JournalMealFood createJournalMealFoodFromFood(JournalMeal jm, Food food, BigDecimal quantity);

    JournalMealFood updateJournalMealFood(Long id, BigDecimal quantity, BigDecimal portionSize, String foodName);

    JournalMealFood deleteJmfByIdAndMealId(Long id, Long mealId);

    void deleteJmf(JournalMealFood jmf);

    JournalMealFood getJournalMealFoodById(Long id);
}
