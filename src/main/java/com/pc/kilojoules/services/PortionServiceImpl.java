package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Portion;
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
    public List<Portion> findPortionsByFood(Food food) {
        return portionRepository.findPortionsByFood(food);
    }

    @Override
    public Portion save(Portion portion) {
        return portionRepository.save(portion);
    }

    @Override
    public Portion deletePortionById(Long id) {
        Portion portion = portionRepository.findById(id).orElseThrow();
        portionRepository.delete(portion);
        return portion;
    }
}
