package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FoodService {

    Page<Food> getFoodsByPage(int page);

    Food getFoodById(Long id);

    Food updateFood(Food food);

    Food createFoodWithPortions(Food food);

    Food deleteFoodById(Long id);

    List<Food> searchFood(String query);

}
