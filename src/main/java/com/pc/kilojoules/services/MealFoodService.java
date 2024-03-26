package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.models.MealFormDTO;
import org.springframework.transaction.annotation.Transactional;

public interface MealFoodService {

    MealFood save(MealFood mealFood);

    @Transactional
    void deleteMealFoodByMealIdAndFoodId(Long mealId, Long foodId);

    MealFood getMealFoodByMealIdAndFoodId(Long mealId, Long foodId);

    boolean existsMealFoodByMealIdAndFoodId(Long mealId, Long foodId);

    void updateMealFood(Long mealId, Long foodId, MealFormDTO mealFormDTO);
}
