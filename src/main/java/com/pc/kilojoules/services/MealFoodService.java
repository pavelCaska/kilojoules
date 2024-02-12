package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.MealFood;

public interface MealFoodService {

    MealFood save(MealFood mealFood);

    void deleteMealFoodByMealIdAndFoodId(Long mealId, Long foodId);
}
