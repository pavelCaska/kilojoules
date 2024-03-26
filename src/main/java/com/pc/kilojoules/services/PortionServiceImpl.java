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

    @Autowired
    public PortionServiceImpl(PortionRepository portionRepository) {
        this.portionRepository = portionRepository;
    }

    @Override
    public List<Portion> getPortionsByFood(Food food) {
        return portionRepository.findPortionsByFood(food);
    }

    @Override
    public Portion createPortion(Food food, Portion portion) throws RecordNameExistsException {
        List<Portion> portionList = getPortionsByFood(food);
        for (Portion item : portionList) {
            if(item.getPortionName().equals(portion.getPortionName())) {
                throw new RecordNameExistsException("Portion name already exists for this food.");
            }
        }
        Portion newPortion = Portion.builder()
                .portionName(portion.getPortionName())
                .portionSize(portion.getPortionSize())
                .food(food)
                .build();
        return portionRepository.save(newPortion);
    }

    @Override
    public Portion deletePortionById(Long id) throws RecordNotDeletableException {
        Portion portion = portionRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Portion record with id \" + id + \" does not exist!"));
        if(portion.getPortionName().equals("100 g") || portion.getPortionName().equals("1 g")) {
            throw new RecordNotDeletableException("This record cannot be deleted.");
        }
        portionRepository.delete(portion);
        return portion;
    }
}
