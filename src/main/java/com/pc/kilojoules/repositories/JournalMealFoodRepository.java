package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.JournalMeal;
import com.pc.kilojoules.entities.JournalMealFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JournalMealFoodRepository extends JpaRepository<JournalMealFood, Long> {

    List<JournalMealFood> findAllByJournalMeal(JournalMeal journalMeal);

}