package com.pc.kilojoules.bootstrap;

import com.pc.kilojoules.entities.*;
import com.pc.kilojoules.models.FoodCSVRecord;
import com.pc.kilojoules.repositories.*;
import com.pc.kilojoules.services.FoodCsvService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.pc.kilojoules.constant.Constant.ONE_HUNDRED;

@Component
public class BootstrapData implements CommandLineRunner {

    private final FoodRepository foodRepository;
    private final FoodCsvService foodCsvService;
    private final PortionRepository portionRepository;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;
    private final JournalRepository journalRepository;
    private final JournalFoodRepository journalFoodRepository;
    private final JournalFoodPortionRepository journalFoodPortionRepository;
    private final JournalMealRepository journalMealRepository;
    private final JournalMealFoodRepository journalMealFoodRepository;
    private final JournalMealFoodPortionRepository journalMealFoodPortionRepository;

    public BootstrapData(FoodRepository foodRepository, FoodCsvService foodCsvService, PortionRepository portionRepository, MealRepository mealRepository, MealFoodRepository mealFoodRepository, JournalRepository journalRepository, JournalFoodRepository journalFoodRepository,
                         JournalFoodPortionRepository journalFoodPortionRepository, JournalMealRepository journalMealRepository, JournalMealFoodRepository journalMealFoodRepository, JournalMealFoodPortionRepository journalMealFoodPortionRepository) {
        this.foodRepository = foodRepository;
        this.foodCsvService = foodCsvService;
        this.portionRepository = portionRepository;
        this.mealRepository = mealRepository;
        this.mealFoodRepository = mealFoodRepository;
        this.journalRepository = journalRepository;
        this.journalFoodRepository = journalFoodRepository;
        this.journalFoodPortionRepository = journalFoodPortionRepository;
        this.journalMealRepository = journalMealRepository;
        this.journalMealFoodRepository = journalMealFoodRepository;
        this.journalMealFoodPortionRepository = journalMealFoodPortionRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadCsvData();
        populatePortions();
        populateIndividualPortions();
        populateIndividualMeals();
        populateJournalWithFoods();
        populateJournalWithMeals();

    }

    private void loadCsvData() throws FileNotFoundException {
        if (foodRepository.count() < 10){
            File file = ResourceUtils.getFile("classpath:csvdata/potraviny.csv");

            List<FoodCSVRecord> recs = foodCsvService.convertCSV(file);

            recs.forEach(foodCSVRecord -> {

                foodRepository.save(Food.builder()
                                .name(foodCSVRecord.getName())
                                .quantity(foodCSVRecord.getQuantity() != null ? foodCSVRecord.getQuantity() : BigDecimal.ZERO)
                                .kiloJoules(foodCSVRecord.getKiloJoules() != null ? foodCSVRecord.getKiloJoules() : BigDecimal.ZERO)
                                .proteins(foodCSVRecord.getProteins() != null ? foodCSVRecord.getProteins() : BigDecimal.ZERO)
                                .carbohydrates(foodCSVRecord.getCarbohydrates() != null ? foodCSVRecord.getCarbohydrates() : BigDecimal.ZERO)
                                .fiber(foodCSVRecord.getFiber() != null ? foodCSVRecord.getFiber() : BigDecimal.ZERO)
                                .sugar(foodCSVRecord.getSugar() != null ? foodCSVRecord.getSugar() : BigDecimal.ZERO)
                                .fat(foodCSVRecord.getFat() != null ? foodCSVRecord.getFat() : BigDecimal.ZERO)
                                .safa(foodCSVRecord.getSafa() != null ? foodCSVRecord.getSafa() : BigDecimal.ZERO)
                                .tfa(foodCSVRecord.getTfa() != null ? foodCSVRecord.getTfa() : BigDecimal.ZERO)
                                .cholesterol(foodCSVRecord.getCholesterol() != null ? foodCSVRecord.getCholesterol() : BigDecimal.ZERO)
                                .sodium(foodCSVRecord.getSodium() != null ? foodCSVRecord.getSodium() : BigDecimal.ZERO)
                                .calcium(foodCSVRecord.getCalcium() != null ? foodCSVRecord.getCalcium() : BigDecimal.ZERO)
                                .phe(foodCSVRecord.getPhe() != null ? foodCSVRecord.getPhe() : BigDecimal.ZERO)
                                .createdAt(foodCSVRecord.getCreatedAt())
                        .build());
            });
        }
    }

    private void populatePortions() {
        List<Food> foodList = foodRepository.findAll();
        foodList.forEach(food -> {
            Portion portion = Portion.builder()
                    .portionName("1 g")
                    .portionSize(BigDecimal.valueOf(1))
                    .food(food)
                    .build();
            portionRepository.save(portion);
        });
        foodList.forEach((Food food) -> {
            Portion portion = Portion.builder()
                    .portionName("100 g")
                    .portionSize(BigDecimal.valueOf(100))
                    .food(food)
                    .build();
            portionRepository.save(portion);
        });
    }
    private void populateIndividualPortions() {
        Food food1 = foodRepository.findById(1L).orElseThrow(); // omacka Kaiser bolognese
        Portion portion1 = Portion.builder()
                    .portionName("balení 350 g")
                    .portionSize(BigDecimal.valueOf(350))
                    .food(food1)
                    .build();
        portionRepository.save(portion1);

        Food food2 = foodRepository.findById(2L).orElseThrow(); // Kitchin tunak
        Portion portion2 = Portion.builder()
                    .portionName("pevný podíl 150 g")
                    .portionSize(BigDecimal.valueOf(150))
                    .food(food2)
                    .build();
        portionRepository.save(portion2);

        Food food7 = foodRepository.findById(7L).orElseThrow(); // brambory
        Portion portion7 = Portion.builder()
                .portionName("standard 250 g")
                .portionSize(BigDecimal.valueOf(250))
                .food(food7)
                .build();
        portionRepository.save(portion7);

        Food food9 = foodRepository.findById(9L).orElseThrow(); // cibule
        Portion portion9 = Portion.builder()
                .portionName("malý kus 50 g")
                .portionSize(BigDecimal.valueOf(50))
                .food(food9)
                .build();
        portionRepository.save(portion9);

        Portion portion10 = Portion.builder()
                .portionName("střední kus 70 g")
                .portionSize(BigDecimal.valueOf(70))
                .food(food9)
                .build();
        portionRepository.save(portion10);

        Portion portion11 = Portion.builder()
                .portionName("velký kus 100 g")
                .portionSize(BigDecimal.valueOf(100))
                .food(food9)
                .build();
        portionRepository.save(portion11);

        Food food12 = foodRepository.findById(12L).orElseThrow(); // michana vejce
        Portion portion12 = Portion.builder()
                .portionName("malý kus 50 g")
                .portionSize(BigDecimal.valueOf(50))
                .food(food12)
                .build();
        portionRepository.save(portion12);
    }
    public void populateIndividualMeals() {

        Meal bologneseSBrambory = Meal.builder()
                .mealName("Bolognese s brambory")
                .build();
        mealRepository.save(bologneseSBrambory);

        MealFood mealFoodOmacka = MealFood.builder()
                .meal(bologneseSBrambory)
                .food(foodRepository.findById(1L).orElseThrow())
                .quantity(BigDecimal.valueOf(350))
                .build();
        mealFoodRepository.save(mealFoodOmacka);

        MealFood mealFoodBrambory = MealFood.builder()
                .meal(bologneseSBrambory)
                .food(foodRepository.findById(7L).orElseThrow())
                .quantity(BigDecimal.valueOf(250))
                .build();
        mealFoodRepository.save(mealFoodBrambory);

        Set<MealFood> bolognese = new HashSet<>();
        bolognese.add(mealFoodOmacka);
        bolognese.add(mealFoodBrambory);

        bologneseSBrambory.setMealFoods(bolognese);
        mealRepository.save(bologneseSBrambory);

        Meal vejceSCibuli = Meal.builder()
                .mealName("Vejce s cibulí")
                .build();
        mealRepository.save(vejceSCibuli);

        MealFood mealFoodVejce = MealFood.builder()
                .meal(vejceSCibuli)
                .food(foodRepository.findById(12L).orElseThrow())
                .quantity(BigDecimal.valueOf(150))
                .build();
        mealFoodRepository.save(mealFoodVejce);

        MealFood mealFoodCibule = MealFood.builder()
                .meal(vejceSCibuli)
                .food(foodRepository.findById(9L).orElseThrow())
                .quantity(BigDecimal.valueOf(70))
                .build();
        mealFoodRepository.save(mealFoodCibule);

        Set<MealFood> vejceCibule = new HashSet<>();
        vejceCibule.add(mealFoodVejce);
        vejceCibule.add(mealFoodCibule);

        vejceSCibuli.setMealFoods(vejceCibule);
        mealRepository.save(vejceSCibuli);
    }

    private void populateJournalWithFoods() {
        Food food1 = foodRepository.findById(12L).orElseThrow();
        BigDecimal quantity1 = new BigDecimal("165");

        JournalFood journalFood1 = JournalFood.builder()
                .name(food1.getName())
                .quantity(quantity1)
                .kiloJoules(food1.getKiloJoules().multiply(quantity1).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .proteins(food1.getProteins().multiply(quantity1).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .carbohydrates(food1.getCarbohydrates().multiply(quantity1).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .fat(food1.getFat().multiply(quantity1).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .fiber(food1.getFiber().multiply(quantity1).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .build();
        JournalFood savedJournalFood1 = journalFoodRepository.save(journalFood1);

        List<Portion> portions = portionRepository.findPortionsByFood(food1);
        portions.forEach(portion -> {
            JournalFoodPortion journalFoodPortion1 = JournalFoodPortion.builder()
                    .journalFood(savedJournalFood1)
                    .portionName(portion.getPortionName())
                    .portionSize(portion.getPortionSize())
                    .build();
            journalFoodPortionRepository.save(journalFoodPortion1);
        });

        LocalDate date = LocalDate.now();
        Journal journal1 = Journal.builder()
                .consumedAt(date)
                .mealType(MealType.BREAKFAST)
                .journalFood(journalFood1)
                .journalMeal(null)
                .build();
        journalRepository.save(journal1);

        Food food2 = foodRepository.findById(9L).orElseThrow();
        BigDecimal quantity2 = new BigDecimal("50");

        JournalFood journalFood2 = JournalFood.builder()
                .name(food2.getName())
                .quantity(quantity2)
                .kiloJoules(food2.getKiloJoules().multiply(quantity2).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .proteins(food2.getProteins().multiply(quantity2).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .carbohydrates(food2.getCarbohydrates().multiply(quantity2).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .fat(food2.getFat().multiply(quantity2).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .fiber(food2.getFiber().multiply(quantity2).divide(ONE_HUNDRED, RoundingMode.HALF_UP))
                .build();
        JournalFood savedJournalFood2 = journalFoodRepository.save(journalFood2);

        List<Portion> portions2 = portionRepository.findPortionsByFood(food2);
        portions2.forEach((Portion portion) -> {
            JournalFoodPortion journalFoodPortion2 = JournalFoodPortion.builder()
                    .journalFood(savedJournalFood2)
                    .portionName(portion.getPortionName())
                    .portionSize(portion.getPortionSize())
                    .build();
            journalFoodPortionRepository.save(journalFoodPortion2);
        });

        Journal journal2 = Journal.builder()
                .consumedAt(date)
                .mealType(MealType.LUNCH)
                .journalFood(journalFood2)
                .journalMeal(null)
                .build();
        journalRepository.save(journal2);
    }

    private void populateJournalWithMeals() {

        Meal meal1 = mealRepository.findById(1L).orElseThrow(); // Bolognese s brambory

        JournalMeal journalMeal1 = JournalMeal.builder()
                .mealName(meal1.getMealName())
                .saved(false)
                .build();
        journalMealRepository.save(journalMeal1);

        Set<JournalMealFood> journalMealFoods1 = new HashSet<>();
        for (MealFood mealFood : meal1.getMealFoods()) {
            JournalMealFood journalMealFood = new JournalMealFood();
            BigDecimal quantity = mealFood.getQuantity();
            Food food = mealFood.getFood();
            journalMealFood.setName(food.getName());
            journalMealFood.setQuantity(quantity);
            journalMealFood.setKiloJoules(food.getKiloJoules().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setProteins(food.getProteins().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setCarbohydrates(food.getCarbohydrates().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setFiber(food.getFiber().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setFat(food.getFat().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setJournalMeal(journalMeal1);
            journalMealFood = journalMealFoodRepository.save(journalMealFood);
            final JournalMealFood finalJournalMealFood1 = journalMealFood;
            List<JournalMealFoodPortion> portionList = new ArrayList<>();

            List<Portion> portions = portionRepository.findPortionsByFood(food);
            portions.forEach(portion -> {
                JournalMealFoodPortion journalMealFoodPortion = JournalMealFoodPortion.builder()
                        .portionName(portion.getPortionName())
                        .portionSize(portion.getPortionSize())
                        .journalMealFood(finalJournalMealFood1)
                        .build();
                journalMealFoodPortionRepository.save(journalMealFoodPortion);
                portionList.add(journalMealFoodPortion);
            });
            journalMealFood.setPortions(portionList);

            journalMealFoods1.add(journalMealFood);
        }
        journalMeal1.setJournalMealFoods(journalMealFoods1);

        journalMeal1.setQuantity(journalMealFoods1.stream().map(JournalMealFood::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal1.setKiloJoules(journalMealFoods1.stream().map(JournalMealFood::getKiloJoules).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal1.setProteins(journalMealFoods1.stream().map(JournalMealFood::getProteins).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal1.setCarbohydrates(journalMealFoods1.stream().map(JournalMealFood::getCarbohydrates).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal1.setFiber(journalMealFoods1.stream().map(JournalMealFood::getFiber).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal1.setFat(journalMealFoods1.stream().map(JournalMealFood::getFat).reduce(BigDecimal.ZERO,BigDecimal::add));

        journalMeal1.setSaved(true);
        journalMealRepository.save(journalMeal1);

        LocalDate date = LocalDate.now();
        Journal journal1 = Journal.builder()
                .consumedAt(date)
                .mealType(MealType.LUNCH)
                .journalFood(null)
                .journalMeal(journalMeal1)
                .build();
        journalRepository.save(journal1);

        Meal meal2 = mealRepository.findById(2L).orElseThrow(); // Vejce s cibuli
        JournalMeal journalMeal2 = JournalMeal.builder()
                .mealName(meal2.getMealName())
                .saved(false)
                .build();
        journalMealRepository.save(journalMeal2);

        Set<JournalMealFood> journalMealFoods2 = new HashSet<>();
        for (MealFood mealFood : meal2.getMealFoods()) {
            JournalMealFood journalMealFood = new JournalMealFood();
            BigDecimal quantity = mealFood.getQuantity();
            Food food = mealFood.getFood();
            journalMealFood.setName(food.getName());
            journalMealFood.setQuantity(quantity);
            journalMealFood.setKiloJoules(food.getKiloJoules().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setProteins(food.getProteins().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setCarbohydrates(food.getCarbohydrates().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setFiber(food.getFiber().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setFat(food.getFat().multiply(quantity).divide(ONE_HUNDRED, RoundingMode.HALF_UP));
            journalMealFood.setJournalMeal(journalMeal2);
            journalMealFood = journalMealFoodRepository.save(journalMealFood);
            final JournalMealFood finalJournalMealFood2 = journalMealFood;
            List<JournalMealFoodPortion> portionList = new ArrayList<>();

            List<Portion> portions = portionRepository.findPortionsByFood(food);
            portions.forEach((Portion portion) -> {
                JournalMealFoodPortion journalMealFoodPortion = JournalMealFoodPortion.builder()
                        .portionName(portion.getPortionName())
                        .portionSize(portion.getPortionSize())
                        .journalMealFood(finalJournalMealFood2)
                        .build();
                journalMealFoodPortionRepository.save(journalMealFoodPortion);
                portionList.add(journalMealFoodPortion);
            });
            journalMealFood.setPortions(portionList);



            journalMealFoods2.add(journalMealFood);
        }
        journalMeal2.setJournalMealFoods(journalMealFoods2);

        journalMeal2.setQuantity(journalMealFoods2.stream().map(JournalMealFood::getQuantity).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal2.setKiloJoules(journalMealFoods2.stream().map(JournalMealFood::getKiloJoules).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal2.setProteins(journalMealFoods2.stream().map(JournalMealFood::getProteins).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal2.setCarbohydrates(journalMealFoods2.stream().map(JournalMealFood::getCarbohydrates).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal2.setFiber(journalMealFoods2.stream().map(JournalMealFood::getFiber).reduce(BigDecimal.ZERO,BigDecimal::add));
        journalMeal2.setFat(journalMealFoods2.stream().map(JournalMealFood::getFat).reduce(BigDecimal.ZERO,BigDecimal::add));

        journalMeal2.setSaved(true);
        journalMealRepository.save(journalMeal2);

        Journal journal2 = Journal.builder()
                .consumedAt(date)
                .mealType(MealType.BREAKFAST)
                .journalFood(null)
                .journalMeal(journalMeal2)
                .build();
        journalRepository.save(journal2);
    }
}
