package com.pc.kilojoules.repositories;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortionRepository extends JpaRepository<Portion, Long> {
    List<Portion> findPortionsByFood(Food food);
}