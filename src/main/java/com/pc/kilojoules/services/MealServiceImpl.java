package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.models.MealDTO;
import com.pc.kilojoules.models.MealFoodDTO;
import com.pc.kilojoules.models.MealFormDTO;
import com.pc.kilojoules.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final FoodService foodService;
    private final MealFoodService mealFoodService;

    @Autowired
    public MealServiceImpl(MealRepository mealRepository, FoodService foodService, @Lazy MealFoodService mealFoodService) {
        this.mealRepository = mealRepository;
        this.foodService = foodService;
        this.mealFoodService = mealFoodService;
    }

    @Override
    public Meal save(Meal meal) {
        return mealRepository.save(meal);
    }

    @Override
    public Page<Meal> getMealsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return mealRepository.findAll(pageable);
    }

    @Override
    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Meal record with id " + id + " does not exist!"));
    }

    @Override
    public Meal createMeal(MealFormDTO mealFormDTO, List<Long> foods) {
        Meal meal = Meal.builder()
                .mealName(mealFormDTO.getMealName())
                .build();
        mealRepository.save(meal);

        BigDecimal savedQuantity = mealFormDTO.getQuantity().multiply(mealFormDTO.getPortionSize());
        Set<MealFood> mealFoods = getMealFoodSet(foods, meal, savedQuantity);
        meal.setMealFoods(mealFoods);
        return mealRepository.save(meal);
    }

    private Set<MealFood> getMealFoodSet(List<Long> foods, Meal meal, BigDecimal savedQuantity) {
        return foods.stream()
                .map(foodId -> {
                    MealFood mf = new MealFood();
                    mf.setMeal(meal);
                    mf.setQuantity(savedQuantity);
                    Food food = foodService.getFoodById(foodId);
                    mf.setFood(food);
                    return mealFoodService.save(mf);
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Meal addFoodToMeal(Long id, MealFormDTO mealFormDTO, List<Long> foods) {
        Meal meal = getMealById(id);
        meal.setMealName(mealFormDTO.getMealName());

        BigDecimal savedQuantity = mealFormDTO.getQuantity().multiply(mealFormDTO.getPortionSize());
        Set<MealFood> newMealFoodSet = getMealFoodSet(foods, meal, savedQuantity);
        meal.getMealFoods().addAll(newMealFoodSet);
        return mealRepository.save(meal);
    }

    @Override
    public Meal updateMealName(Long id, String mealName) {
        if(mealName == null || mealName.isEmpty() || !mealName.matches(".*\\S+.*") || mealName.length() > 75){
            throw new IllegalArgumentException("Provided mealName is either null, empty, has only whitespaces or its length is greater than 75 characters.");
        }
        Meal meal = mealRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Meal record with id " + id + " does not exist!"));
        meal.setMealName(mealName);
        return mealRepository.save(meal);
}

    @Override
    public List<MealDTO> calculateAndReturnMealDtoList(List<Meal> meals) {
        return meals.stream().map(this::getMealDTO).collect(Collectors.toList());
    }
    @Override
    public MealDTO calculateAndReturnMealDto(Long id) {
        Meal meal = this.getMealById(id);
        return getMealDTO(meal);
    }
    private MealDTO getMealDTO(Meal meal) {
        MealDTO mealDTO = new MealDTO();
        mealDTO.setMealName(meal.getMealName());
        mealDTO.setMealId(meal.getId());

        List<MealFoodDTO> mealFoodsDTO = this.calculateAndReturnAdjustedMealFoods(meal);
        mealDTO.setMealFoodsDTO(mealFoodsDTO);

        this.sumUpMealFoods(mealDTO, mealFoodsDTO);

        return mealDTO;
    }
    @Override
    public List<MealFoodDTO> calculateAndReturnAdjustedMealFoods(Meal meal) {
        return meal.getMealFoods().stream().map(mealFood -> {
            MealFoodDTO dto = new MealFoodDTO();
            dto.setId(mealFood.getId());
            dto.setFoodId(mealFood.getFood().getId());
            dto.setFoodName(mealFood.getFood().getName());

            BigDecimal quantity = mealFood.getQuantity();
            BigDecimal divisor = mealFood.getFood().getQuantity();

            dto.setQuantity(quantity);
            dto.setAdjustedKiloJoules(mealFood.getFood().getKiloJoules().multiply(quantity).divide(divisor, RoundingMode.HALF_UP));
            dto.setAdjustedProteins(mealFood.getFood().getProteins().multiply(quantity).divide(divisor, RoundingMode.HALF_UP));
            dto.setAdjustedCarbohydrates(mealFood.getFood().getCarbohydrates().multiply(quantity).divide(divisor, RoundingMode.HALF_UP));
            dto.setAdjustedFiber(mealFood.getFood().getFiber().multiply(quantity).divide(divisor, RoundingMode.HALF_UP));
            dto.setAdjustedFat(mealFood.getFood().getFat().multiply(quantity).divide(divisor, RoundingMode.HALF_UP));

            return dto;
        }).collect(Collectors.toList());
    }
    @Override
    public void sumUpMealFoods(MealDTO mealDTO, List<MealFoodDTO> mealFoodsDTO) {
        mealDTO.setSumQuantity(mealFoodsDTO.stream().map(MealFoodDTO::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add));
        mealDTO.setSumAdjustedKiloJoules(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedKiloJoules).reduce(BigDecimal.ZERO,BigDecimal::add));
        mealDTO.setSumAdjustedProteins(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedProteins).reduce(BigDecimal.ZERO,BigDecimal::add));
        mealDTO.setSumAdjustedCarbohydrates(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedCarbohydrates).reduce(BigDecimal.ZERO,BigDecimal::add));
        mealDTO.setSumAdjustedFiber(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedFiber).reduce(BigDecimal.ZERO,BigDecimal::add));
        mealDTO.setSumAdjustedFat(mealFoodsDTO.stream().map(MealFoodDTO::getAdjustedFat).reduce(BigDecimal.ZERO,BigDecimal::add));
    }

    @Override
    public Meal deleteMealById(Long id) {
        Meal meal = mealRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Meal record with id " + id + " does not exist!"));
        mealRepository.delete(meal);
        return meal;
    }
}
