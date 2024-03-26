package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;

import java.util.List;

public interface PortionService {
    List<Portion> getPortionsByFood(Food food);

    Portion createPortion(Food food, Portion portion);

    Portion deletePortionById(Long id);
}
