package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;
import com.pc.kilojoules.exceptions.RecordNameExistsException;
import com.pc.kilojoules.exceptions.RecordNotDeletableException;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.repositories.PortionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortionServiceImpl implements PortionService {

    private final PortionRepository portionRepository;
    private final FoodService foodService;

    @Autowired
    public PortionServiceImpl(PortionRepository portionRepository, FoodService foodService) {
        this.portionRepository = portionRepository;
        this.foodService = foodService;
    }


    @Override
    public Portion createPortion(Food food, Portion portion) throws RecordNameExistsException {
        List<Portion> portionList = food.getPortions();
        for (Portion item : portionList) {
            if(item.getPortionName().equals(portion.getPortionName())) {
                throw new RecordNameExistsException("Portion name already exists for this food.");
            }
        }
        portionRepository.save(portion);
        portionList.add(portion);
        food.setPortions(portionList);
        foodService.saveFood(food);
        return portion;
    }

    @Override
    public Portion deletePortionById(Long id) throws RecordNotDeletableException {
        Portion portion = portionRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Portion record with id \" + id + \" does not exist!"));
        if(portion.getPortionName().equals("100 g") || portion.getPortionName().equals("1 g")) {
            throw new RecordNotDeletableException("This record cannot be deleted.");
        }
        List<Portion> portionList = portion.getFood().getPortions();
        portionList.remove(portion);
        portion.getFood().setPortions(portionList);
        foodService.saveFood(portion.getFood());
        portionRepository.delete(portion);
        return portion;
    }
    @Override
    public boolean existsPortionByIdAndFoodId(Long portionId, Long foodId) {
        return portionRepository.findPortionByIdAndFoodId(portionId, foodId).isPresent();
    }

}
