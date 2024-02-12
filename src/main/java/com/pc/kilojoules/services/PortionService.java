package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;

import java.util.List;

public interface PortionService {
    List<Portion> findPortionsByFood(Food food);

    Portion save(Portion portion);

    Portion deletePortionById(Long id);
}
