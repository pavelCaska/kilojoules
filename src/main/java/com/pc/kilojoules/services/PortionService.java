package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;

public interface PortionService {

    Portion createPortion(Food food, Portion portion);

    Portion deletePortionById(Long id);

    boolean existsPortionByIdAndFoodId(Long portionId, Long foodId);
}
