package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.JournalFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JournalFoodRepository extends JpaRepository<JournalFood, Long> {

}