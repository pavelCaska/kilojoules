package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.repositories.MealFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MealFoodServiceImpl implements MealFoodService {

    private final MealFoodRepository mealFoodRepository;

    @Autowired
    public MealFoodServiceImpl(MealFoodRepository mealFoodRepository) {
        this.mealFoodRepository = mealFoodRepository;
    }

    @Override
    public MealFood save(MealFood mealFood) {
        return mealFoodRepository.save(mealFood);
    }

    @Override
    @Transactional
    public void deleteMealFoodByMealIdAndFoodId(Long mealId, Long foodId) {
        mealFoodRepository.deleteMealFoodByMealIdAndFoodId(mealId, foodId);
    }
    @Override
    public MealFood findMealFoodByMealIdAndFoodId(Long mealId, Long foodId) {
        return mealFoodRepository.findMealFoodByMealIdAndFoodId(mealId, foodId).orElseThrow();
    }
}
