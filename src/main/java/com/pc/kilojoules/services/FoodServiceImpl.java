package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.repositories.FoodRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;

    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public List<Food> findAllByUpdatedDesc() {
        return foodRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));
    }

    @Override
    public Food findById(Long id) {
        return foodRepository.findById(id).orElseThrow();
    }

    @Override
    public Food update(Food food) {
        return foodRepository.save(food);
    }

    @Override
    public Food create(Food food) {
        Date date = new Date();
        food.setCreatedAt(date);
        return foodRepository.save(food);
    }

    @Override
    public Food deleteFoodById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow();
        foodRepository.delete(food);
        return food;
    }

    @Override
    public List<Food> searchFood(String query) {
        return foodRepository.findAllByNameContainsIgnoreCase(query);
    }

    @Override
    public Page<Food> getFoodsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "updatedAt")); // 10 foods per page
        return foodRepository.findAll(pageable);
    }

}
