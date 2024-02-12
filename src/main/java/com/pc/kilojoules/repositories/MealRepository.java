package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
    void deleteById(Long id);

}