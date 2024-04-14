package com.pc.kilojoules.services;

import com.pc.kilojoules.entities.*;
import com.pc.kilojoules.exceptions.RecordNotFoundException;
import com.pc.kilojoules.repositories.JournalMealRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.pc.kilojoules.constant.Constant.ONE_HUNDRED;

@Service
public class JournalMealServiceImpl implements JournalMealService {
    private final JournalMealRepository journalMealRepository;
    private final JournalMealFoodPortionService journalMealFoodPortionService;
    private final JournalMealFoodService journalMealFoodService;
    private final FoodService foodService;
    private static final Logger log = LoggerFactory.getLogger(JournalMealService.class);


    @Autowired
    public JournalMealServiceImpl(JournalMealRepository journalMealRepository, JournalMealFoodPortionService journalMealFoodPortionService, JournalMealFoodService journalMealFoodService, FoodService foodService) {
        this.journalMealRepository = journalMealRepository;
        this.journalMealFoodPortionService = journalMealFoodPortionService;
        this.journalMealFoodService = journalMealFoodService;
        this.foodService = foodService;
    }

    @Override
    @Transactional
    public JournalMeal createJournalMeal(Long id, String mealName) {
        JournalMeal journalMeal = journalMealRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Meal record with id " + id + " does not exist!"));
        journalMeal.setSaved(true);
        journalMeal.setMealName(mealName);
        return journalMealRepository.save(journalMeal);
    }

    @Override
    @Transactional
    public JournalMeal convertMealToUnsavedJournalMeal(Meal meal) {

        JournalMeal journalMeal = JournalMeal.builder()
                .mealName(meal.getMealName())
                .saved(false)
                .build();
        journalMealRepository.save(journalMeal);

        Set<JournalMealFood> jmfSet = meal.getMealFoods().stream()
                .map(mealFood -> createJournalMealFoodFromMealFood(mealFood, journalMeal))
                .collect(Collectors.toSet());

        journalMeal.setJournalMealFoods(jmfSet);
        calculateAndSetTotalFieldsFromSet(journalMeal, jmfSet);

        return journalMealRepository.save(journalMeal);
    }

    private JournalMealFood createJournalMealFoodFromMealFood(MealFood mealFood, JournalMeal journalMeal) {
        JournalMealFood jmf = new JournalMealFood();
        BigDecimal quantity = mealFood.getQuantity();
        Food food = mealFood.getFood();
        jmf.setName(food.getName());
        jmf.setQuantity(quantity);
        jmf.setKiloJoules(food.getKiloJoules().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setProteins(food.getProteins().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setCarbohydrates(food.getCarbohydrates().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setSugar(food.getSugar().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setFiber(food.getFiber().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setFat(food.getFat().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setSafa(food.getSafa().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setTfa(food.getTfa().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setCholesterol(food.getCholesterol().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setSodium(food.getSodium().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setCalcium(food.getCalcium().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setPhe(food.getPhe().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
        jmf.setJournalMeal(journalMeal);
        jmf = journalMealFoodService.saveJournalMealFood(jmf);
        final JournalMealFood finalJmf = jmf;

        List<JournalMealFoodPortion> portionList = food.getPortions().stream().map(portion -> {
            JournalMealFoodPortion jmfp = JournalMealFoodPortion.builder()
                    .portionName(portion.getPortionName())
                    .portionSize(portion.getPortionSize())
                    .journalMealFood(finalJmf)
                    .build();
            journalMealFoodPortionService.saveJmfp(jmfp);
            return jmfp;
        }).collect(Collectors.toList());
        jmf.setPortions(portionList);
        return journalMealFoodService.saveJournalMealFood(jmf);
    }

    @Override
    public void calculateAndSetTotalFieldsFromSet(JournalMeal journalMeal, Set<JournalMealFood> jmfSet) {
        journalMeal.setQuantity(totalField(jmfSet, JournalMealFood::getQuantity));
        journalMeal.setKiloJoules(totalField(jmfSet, JournalMealFood::getKiloJoules));
        journalMeal.setProteins(totalField(jmfSet, JournalMealFood::getProteins));
        journalMeal.setCarbohydrates(totalField(jmfSet, JournalMealFood::getCarbohydrates));
        journalMeal.setFiber(totalField(jmfSet, JournalMealFood::getFiber));
        journalMeal.setFat(totalField(jmfSet, JournalMealFood::getFat));
        journalMeal.setSugar(totalField(jmfSet, JournalMealFood::getSugar));
        journalMeal.setSafa(totalField(jmfSet, JournalMealFood::getSafa));
        journalMeal.setTfa(totalField(jmfSet, JournalMealFood::getTfa));
        journalMeal.setCholesterol(totalField(jmfSet, JournalMealFood::getCholesterol));
        journalMeal.setSodium(totalField(jmfSet, JournalMealFood::getSodium));
        journalMeal.setCalcium(totalField(jmfSet, JournalMealFood::getCalcium));
        journalMeal.setPhe(totalField(jmfSet, JournalMealFood::getPhe));
    }

    private BigDecimal totalField(Set<JournalMealFood> jmSet, Function<JournalMealFood, BigDecimal> function) {
        return jmSet.stream().map(function).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public JournalMeal getJournalMealById(Long id) {
        return journalMealRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Meal record with id " + id + " does not exist!"));
    }

    @Override
    @Transactional
    public String addFoodToUnsavedJournalMeal(Long mealId, Long foodId, BigDecimal quantity, BigDecimal portionSize) {
        JournalMeal jm = getJournalMealById(mealId);
        Food food = foodService.getFoodById(foodId);
        BigDecimal savedQuantity = quantity.multiply(portionSize);

        JournalMealFood jmf = journalMealFoodService.createJournalMealFoodFromFood(jm, food, savedQuantity);
        updateSetWithJournalMealFood(jm, jmf);
        addJmfToJournalMealTotals(jm, jmf);
        return jmf.getName();
    }

    @Override
    public void updateSetWithJournalMealFood(JournalMeal jm, JournalMealFood jmf) {
        Set<JournalMealFood> jmfSet = jm.getJournalMealFoods();
        jmfSet.removeIf(journalMealFood -> journalMealFood.getId().equals(jmf.getId()));
        jmfSet.add(jmf);
        jm.setJournalMealFoods(jmfSet);
    }

    @Override
    public void addJmfToJournalMealTotals(JournalMeal jm, JournalMealFood jmf) {
        jm.setQuantity(jm.getQuantity().add(jmf.getQuantity()));
        jm.setKiloJoules(jm.getKiloJoules().add(jmf.getKiloJoules()));
        jm.setProteins(jm.getProteins().add(jmf.getProteins()));
        jm.setCarbohydrates(jm.getCarbohydrates().add(jmf.getCarbohydrates()));
        jm.setFiber(jm.getFiber().add(jmf.getFiber()));
        jm.setFat(jm.getFat().add(jmf.getFat()));
        jm.setSugar(jm.getSugar().add(jmf.getSugar()));
        jm.setSafa(jm.getSafa().add(jmf.getSafa()));
        jm.setTfa(jm.getTfa().add(jmf.getTfa()));
        jm.setCholesterol(jm.getCholesterol().add(jmf.getCholesterol()));
        jm.setSodium(jm.getSodium().add(jmf.getSodium()));
        jm.setCalcium(jm.getCalcium().add(jmf.getCalcium()));
        jm.setPhe(jm.getPhe().add(jmf.getPhe()));
    }

    @Override
    public void removeJmfAndSubstractFromJournalMealTotals(JournalMeal jm, JournalMealFood jmf) {
        jm.setQuantity(jm.getQuantity().subtract(jmf.getQuantity()));
        jm.setKiloJoules(jm.getKiloJoules().subtract(jmf.getKiloJoules()));
        jm.setProteins(jm.getProteins().subtract(jmf.getProteins()));
        jm.setCarbohydrates(jm.getCarbohydrates().subtract(jmf.getCarbohydrates()));
        jm.setFiber(jm.getFiber().subtract(jmf.getFiber()));
        jm.setFat(jm.getFat().subtract(jmf.getFat()));
        jm.setSugar(jm.getSugar().subtract(jmf.getSugar()));
        jm.setSafa(jm.getSafa().subtract(jmf.getSafa()));
        jm.setTfa(jm.getTfa().subtract(jmf.getTfa()));
        jm.setCholesterol(jm.getCholesterol().subtract(jmf.getCholesterol()));
        jm.setSodium(jm.getSodium().subtract(jmf.getSodium()));
        jm.setCalcium(jm.getCalcium().subtract(jmf.getCalcium()));
        jm.setPhe(jm.getPhe().subtract(jmf.getPhe()));
        jm.getJournalMealFoods().remove(jmf);
        journalMealRepository.save(jm);
    }

    @Override
    @Transactional
    public String updateUnsavedJournalMealWithJmf(Long mealId, Long foodId, BigDecimal quantity, BigDecimal portionSize, String foodName) {
        JournalMeal jm = getJournalMealById(mealId);
        JournalMealFood jmf = journalMealFoodService.updateJournalMealFood(foodId, quantity, portionSize, foodName);

        updateSetWithJournalMealFood(jm, jmf);
        calculateAndSetTotalFieldsFromSet(jm, jm.getJournalMealFoods());
        journalMealRepository.save(jm);
        return jmf.getName();
    }

    @Override
    public boolean doJournalMealAndJmfExist(Long mealId, Long foodId) {
        return journalMealRepository.checkEntitiesExistence(mealId, foodId).isPresent();
    }

    @Override
    public void saveJournalMeal(JournalMeal jm) {
        journalMealRepository.save(jm);
    }

    //    Quartz Scheduler
    @Override
    public boolean existsUnsavedJournalMeals() {
        return journalMealRepository.existsBySavedFalse();
    }

    @Override
    public void deleteUnsavedJournalMeals() {
        if(existsUnsavedJournalMeals()) {
            journalMealRepository.deleteAllBySavedFalse();
        }
        log.info("Running deleteUnsavedJournalMeals() method.");
        log.info("Does any unsaved journal meal exist: {}", existsUnsavedJournalMeals());
    }
}