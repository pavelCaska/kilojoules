package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.models.MealDTO;
import com.pc.kilojoules.models.MealFoodDTO;
import com.pc.kilojoules.models.MealFormDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MealService {
    Meal save(Meal meal);

    Page<Meal> getMealsByPage(int page);

    Meal getMealById(Long id);

    Meal createMeal(MealFormDTO mealFormDTO, List<Long> foods);

    Meal addFoodToMeal(Long id, MealFormDTO mealFormDTO, List<Long> foods);

    Meal updateMealName(Long id, String mealName);

    List<MealDTO> calculateAndReturnMealDtoList(List<Meal> meals);

    MealDTO calculateAndReturnMealDto(Long id);

    void sumUpMealFoods(MealDTO mealDTO, List<MealFoodDTO> mealFoodsDTO);

    List<MealFoodDTO> calculateAndReturnAdjustedMealFoods(Meal meal);

    Meal deleteMealById(Long id);
}
