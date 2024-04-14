package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.*;
import com.pc.kilojoules.repositories.JournalFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import static com.pc.kilojoules.constant.Constant.ONE_HUNDRED;

@Service
public class JournalFoodServiceImpl implements JournalFoodService {

    private final JournalFoodRepository journalFoodRepository;
    private final JournalFoodPortionService journalFoodPortionService;

    @Autowired
    public JournalFoodServiceImpl(JournalFoodRepository journalFoodRepository, JournalFoodPortionService journalFoodPortionService) {
        this.journalFoodRepository = journalFoodRepository;
        this.journalFoodPortionService = journalFoodPortionService;
    }

    @Override
    public JournalFood createJournalFood(Food food, BigDecimal quantity, String foodName){
        JournalFood jf = JournalFood.builder()
                .quantity(quantity)
                .name(foodName)
                .kiloJoules(food.getKiloJoules().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .proteins(food.getProteins().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .carbohydrates(food.getCarbohydrates().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .fat(food.getFat().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .fiber(food.getFiber().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .sugar(food.getSugar().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .safa(food.getSafa().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .tfa(food.getTfa().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .cholesterol(food.getCholesterol().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .sodium(food.getSodium().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .calcium(food.getCalcium().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .phe(food.getPhe().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .build();
        JournalFood finalJf = journalFoodRepository.save(jf);
        List<JournalFoodPortion> portionList = food.getPortions().stream().map(portion -> {
            JournalFoodPortion jfp = JournalFoodPortion.builder()
                    .portionName(portion.getPortionName())
                    .portionSize(portion.getPortionSize())
                    .journalFood(finalJf)
                    .build();
            journalFoodPortionService.saveJfp(jfp);
            return jfp;
        }).collect(Collectors.toList());
        jf.setPortions(portionList);
        return journalFoodRepository.save(jf);
    }
}
