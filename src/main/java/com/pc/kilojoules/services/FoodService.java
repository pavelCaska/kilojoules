package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FoodService {

    List<Food> findAllByUpdatedDesc();

    Food findById(Long id);

    Food update(Food food);

    Food create(Food food);

    Food deleteFoodById(Long id);

    List<Food> searchFood(String query);

    Page<Food> getFoodsByPage(int page);
}
