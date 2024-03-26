package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.models.MealFormDTO;
import com.pc.kilojoules.repositories.MealFoodRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
//    @Transactional
    public void deleteMealFoodByMealIdAndFoodId(Long mealId, Long foodId) {
        if (!existsMealFoodByMealIdAndFoodId(mealId, foodId)) {
            throw new IllegalArgumentException("Food record with id " + foodId + " is not associated with Meal record with id " + mealId);
        }
        mealFoodRepository.deleteMealFoodByMealIdAndFoodId(mealId, foodId);
    }

    @Override
    public MealFood getMealFoodByMealIdAndFoodId(Long mealId, Long foodId) {
        return mealFoodRepository.findMealFoodByMealIdAndFoodId(mealId, foodId)
                .orElseThrow(()-> new EntityNotFoundException("Cannot find MealFood with mealId: " + mealId + " and foodId: " + foodId));
    }
    @Override
    public boolean existsMealFoodByMealIdAndFoodId(Long mealId, Long foodId) {
        return mealFoodRepository.findMealFoodByMealIdAndFoodId(mealId, foodId).isPresent();
    }

    @Override
    public void updateMealFood(Long mealId, Long foodId, MealFormDTO mealFormDTO) {
        MealFood mealFood = getMealFoodByMealIdAndFoodId(mealId, foodId);
        BigDecimal savedQuantity = mealFormDTO.getQuantity().multiply(mealFormDTO.getPortionSize());
        mealFood.setQuantity(savedQuantity);
        mealFoodRepository.save(mealFood);
    }
}
