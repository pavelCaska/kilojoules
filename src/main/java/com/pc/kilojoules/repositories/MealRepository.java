package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.Meal;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long>, PagingAndSortingRepository<Meal, Long> {

    List<Meal> findAllByMealNameContainingIgnoreCase(@NotBlank String mealName);

}