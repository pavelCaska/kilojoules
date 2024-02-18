package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.entities.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealFoodRepository extends JpaRepository<MealFood, Meal> {

    void deleteMealFoodByMealIdAndFoodId(Long mealId, Long foodId);

    Optional<MealFood> findMealFoodByMealIdAndFoodId(Long mealId, Long foodId);
}