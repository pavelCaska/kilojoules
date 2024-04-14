package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.JournalFood;

import java.math.BigDecimal;

public interface JournalFoodService {

    JournalFood createJournalFood(Food food, BigDecimal quantity, String foodName);

}
