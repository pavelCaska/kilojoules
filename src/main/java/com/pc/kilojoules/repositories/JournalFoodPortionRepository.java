package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.JournalFoodPortion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalFoodPortionRepository extends JpaRepository<JournalFoodPortion, Long> {
}