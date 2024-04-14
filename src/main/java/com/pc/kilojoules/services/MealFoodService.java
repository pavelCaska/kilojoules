package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.models.MealFormDTO;
import jakarta.transaction.Transactional;

public interface MealFoodService {

    MealFood save(MealFood mealFood);

    MealFood getMealFoodById(Long id);

    @Transactional
    MealFood deleteMealFoodById(Long mealId, Long Id);

    boolean existsMealFoodByMealIdAndId(Long mealId, Long foodId);

    MealFood updateMealFood(Long mealId, Long foodId, MealFormDTO mealFormDTO);
}
