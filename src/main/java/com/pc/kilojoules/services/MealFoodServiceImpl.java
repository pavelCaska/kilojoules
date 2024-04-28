package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.models.MealFormDTO;
import com.pc.kilojoules.repositories.MealFoodRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MealFoodServiceImpl implements MealFoodService {

    private final MealFoodRepository mealFoodRepository;
    private final MealService mealService;

    @Autowired
    public MealFoodServiceImpl(MealFoodRepository mealFoodRepository, MealService mealService) {
        this.mealFoodRepository = mealFoodRepository;
        this.mealService = mealService;
    }

    @Override
    public MealFood save(MealFood mealFood) {
        return mealFoodRepository.save(mealFood);
    }

    @Override
    public MealFood getMealFoodById(Long id) {
        return mealFoodRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("MealFood record with id " + id + " does not exist!"));
    }

    @Override
    @Transactional
    public MealFood deleteMealFoodById(Long mealId, Long id) {
        MealFood mf = mealFoodRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("MealFood record with id " + id + " does not exist!"));
        mealFoodRepository.delete(mf);
        Meal meal = mealService.getMealById(mealId);
        meal.getMealFoods().remove(mf);
        mealService.save(meal);
        return mf;
    }

    @Override
    public boolean existsMealFoodByMealIdAndId(Long mealId, Long id) {
        return mealFoodRepository.findMealFoodByMealIdAndId(mealId, id).isPresent();
    }

    @Override
    @Transactional
    public MealFood updateMealFood(Long mealId, Long mealFoodId, MealFormDTO mealFormDTO) {
        MealFood mealFood = getMealFoodById(mealFoodId);
        Meal meal = mealService.getMealById(mealId);

        BigDecimal savedQuantity = mealFormDTO.getQuantity().multiply(mealFormDTO.getPortionSize());

        mealFood.setQuantity(savedQuantity);
        meal.getMealFoods().removeIf(mf -> mf.getId().equals(mealFood.getId()));
        meal.getMealFoods().add(mealFood);

        mealFoodRepository.save(mealFood);
        mealService.save(meal);
        return mealFood;
    }

    @Override
    public boolean isFoodAssociatedToMealFood(Long foodId) {
        List<MealFood> mealFoodList = mealFoodRepository.findMealFoodByFoodId (foodId);
        return !mealFoodList.isEmpty();
    }
}
