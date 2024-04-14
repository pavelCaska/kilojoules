package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Meal;

import java.util.List;

public interface SearchService {

    List<Food> searchFoodsByName(String query);

    List<Meal> searchMealsByMealName(String query);
}
