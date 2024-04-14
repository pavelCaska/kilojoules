package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.repositories.FoodRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.pc.kilojoules.constant.Constant.ONE_HUNDRED;

@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;

    @Autowired
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public List<Food> fetchAllFoods() {
        return foodRepository.findAll();
    }

    @Override
    public Page<Food> getFoodsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return foodRepository.findAll(pageable);
    }

    @Override
    public Food getFoodById(Long id) {
        return foodRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Food record with id " + id + " does not exist!"));
    }
    @Override
    public void saveFood(Food food) {
        foodRepository.save(food);
    }

    @Override
    public Food updateFood(Food food) {
        Food existingFood = foodRepository.findById(food.getId()).orElseThrow(() -> new RecordNotFoundException("Food record with id " + food.getId() + " does not exist!"));
        BeanUtils.copyProperties(food, existingFood, new String[] {"id", "portions"});
        return foodRepository.save(existingFood);
    }

    @Override
    @Transactional
    public Food createFoodWithPortions(Food food) {
        Food savedFood = foodRepository.save(food);

        List<Portion> portions = new ArrayList<>();
        Portion portion100 = new Portion("100 g", ONE_HUNDRED, savedFood);
        Portion portion1 = new Portion("1 g", BigDecimal.ONE, savedFood);
        portions.add(portion100);
        portions.add(portion1);
        savedFood.setPortions(portions);
        return foodRepository.save(savedFood);
    }

    @Override
    @Transactional
    public Food deleteFoodById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Food record with id " + id + " does not exist!"));
        foodRepository.delete(food);
        return food;
    }

    @Override
    public List<Food> searchFood(String query) {
        return foodRepository.findAllByNameContainsIgnoreCase(query);
    }
}
