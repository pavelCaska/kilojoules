package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {

    Optional<MealFood> findMealFoodByMealIdAndId(Long mealId, Long id);

    List<MealFood> findMealFoodByFoodId(Long foodId);
}