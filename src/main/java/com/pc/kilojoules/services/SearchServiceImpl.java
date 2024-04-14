package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.repositories.FoodRepository;
import com.pc.kilojoules.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final FoodRepository foodRepository;
    private final MealRepository mealRepository;

    @Autowired
    public SearchServiceImpl(FoodRepository foodRepository, MealRepository mealRepository) {
        this.foodRepository = foodRepository;
        this.mealRepository = mealRepository;
    }
    @Override
    public List<Food> searchFoodsByName(String query) {
        return foodRepository.findAllByNameContainsIgnoreCase(query);
    }
    @Override
    public List<Meal> searchMealsByMealName(String query) {
        return mealRepository.findAllByMealNameContainingIgnoreCase(query);
    }
}
