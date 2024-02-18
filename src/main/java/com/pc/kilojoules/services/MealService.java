package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.models.MealDTO;

import java.util.List;

public interface MealService {
    Meal save(Meal meal);

    List<Meal> getAllMeals();


    Meal getMealById(Long id);

    List<MealDTO> calculateAndReturnMealDtoList();

    MealDTO calculateAndReturnMealDto(Long id);

    void deleteMealById(Long id);
}
