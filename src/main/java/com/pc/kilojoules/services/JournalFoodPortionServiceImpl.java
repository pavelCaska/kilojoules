package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.JournalFoodPortion;
import com.pc.kilojoules.repositories.JournalFoodPortionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalFoodPortionServiceImpl implements JournalFoodPortionService {

    private final JournalFoodPortionRepository journalFoodPortionRepository;

    @Autowired
    public JournalFoodPortionServiceImpl(JournalFoodPortionRepository journalFoodPortionRepository) {
        this.journalFoodPortionRepository = journalFoodPortionRepository;
    }

    @Override
    public void saveJfp(JournalFoodPortion jfp) {
        journalFoodPortionRepository.save(jfp);
    }

}
