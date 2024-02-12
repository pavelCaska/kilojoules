package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;

import java.util.List;

public interface FoodService {

    List<Food> findAllByUpdatedDesc();

    Food findById(Long id);

    Food update(Food food);

    Food create(Food food);

    Food deleteFoodById(Long id);

    List<Food> searchFood(String query);
}
