package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.models.MealDTO;
import com.pc.kilojoules.models.MealFoodDTO;
import com.pc.kilojoules.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    @Override
    public Meal save(Meal meal) {

        return mealRepository.save(meal);
    }

    @Override
    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    @Override
    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElseThrow();
    }

    @Override
    public List<MealDTO> calculateAndReturnMealDtoList() {
        List<Meal> meals = getAllMeals();

        List<MealDTO> mealsDTO = meals.stream().map(meal -> {
            MealDTO mealDTO = new MealDTO();
            mealDTO.setMealName(meal.getMealName());
            mealDTO.setMealId(meal.getId());

            List<MealFoodDTO> mealFoodsDTO = meal.getMealFoods().stream().map(mealFood -> {
                MealFoodDTO dto = new MealFoodDTO();
                dto.setFoodId(mealFood.getFood().getId());
                dto.setFoodName(mealFood.getFood().getName());

                BigDecimal quantity = mealFood.getQuantity();
                BigDecimal amount = mealFood.getFood().getAmount();

                dto.setQuantity(quantity);
                dto.setAdjustedKiloJoules(mealFood.getFood().getKiloJoules().multiply(quantity).divide(amount));
                dto.setAdjustedProteins(mealFood.getFood().getProteins().multiply(quantity).divide(amount));
                dto.setAdjustedCarbohydrates(mealFood.getFood().getCarbohydrates().multiply(quantity).divide(amount));

                if (mealFood.getFood().getFiber() != null) {
                    dto.setAdjustedFiber(mealFood.getFood().getFiber().multiply(quantity).divide(amount));
                } else {
                    dto.setAdjustedFiber(BigDecimal.ZERO);
                }

                dto.setAdjustedFat(mealFood.getFood().getFat().multiply(quantity).divide(amount));

                return dto;
            }).collect(Collectors.toList());
            mealDTO.setMealFoodsDTO(mealFoodsDTO);

            mealDTO.setSumQuantity(mealFoodsDTO.stream().map(MealFoodDTO::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedKiloJoules(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedKiloJoules).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedProteins(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedProteins).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedCarbohydrates(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedCarbohydrates).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedFiber(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedFiber).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedFat(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedFat).reduce(BigDecimal.ZERO,BigDecimal::add));

            return mealDTO;
        }).collect(Collectors.toList());

        return mealsDTO;
    }
    @Override
    public MealDTO calculateAndReturnMealDto(Long id) {

        Meal meal = getMealById(id);

            MealDTO mealDTO = new MealDTO();
            mealDTO.setMealName(meal.getMealName());
            mealDTO.setMealId(meal.getId());

            List<MealFoodDTO> mealFoodsDTO = meal.getMealFoods().stream().map(mealFood -> {
                MealFoodDTO dto = new MealFoodDTO();
                dto.setFoodId(mealFood.getFood().getId());
                dto.setFoodName(mealFood.getFood().getName());

                BigDecimal quantity = mealFood.getQuantity();
                BigDecimal amount = mealFood.getFood().getAmount();

                dto.setQuantity(quantity);
                dto.setAdjustedKiloJoules(mealFood.getFood().getKiloJoules().multiply(quantity).divide(amount));
                dto.setAdjustedProteins(mealFood.getFood().getProteins().multiply(quantity).divide(amount));
                dto.setAdjustedCarbohydrates(mealFood.getFood().getCarbohydrates().multiply(quantity).divide(amount));

                if (mealFood.getFood().getFiber() != null) {
                    dto.setAdjustedFiber(mealFood.getFood().getFiber().multiply(quantity).divide(amount));
                } else {
                    dto.setAdjustedFiber(BigDecimal.ZERO);
                }

                dto.setAdjustedFat(mealFood.getFood().getFat().multiply(quantity).divide(amount));

                return dto;
            }).collect(Collectors.toList());
            mealDTO.setMealFoodsDTO(mealFoodsDTO);

            mealDTO.setSumQuantity(mealFoodsDTO.stream().map(MealFoodDTO::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedKiloJoules(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedKiloJoules).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedProteins(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedProteins).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedCarbohydrates(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedCarbohydrates).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedFiber(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedFiber).reduce(BigDecimal.ZERO,BigDecimal::add));
            mealDTO.setSumAdjustedFat(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedFat).reduce(BigDecimal.ZERO,BigDecimal::add));

            return mealDTO;
    }

    @Override
    @Transactional
    public void deleteMealById(Long id) {
        mealRepository.deleteById(id);
    }
}
