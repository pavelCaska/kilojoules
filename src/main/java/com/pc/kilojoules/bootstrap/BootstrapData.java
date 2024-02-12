package com.pc.kilojoules.bootstrap;

import com.pc.kilojoules.entities.Food;
import com.pc.kilojoules.entities.Meal;
import com.pc.kilojoules.entities.MealFood;
import com.pc.kilojoules.entities.Portion;
import com.pc.kilojoules.models.FoodCSVRecord;
import com.pc.kilojoules.repositories.FoodRepository;
import com.pc.kilojoules.repositories.MealFoodRepository;
import com.pc.kilojoules.repositories.MealRepository;
import com.pc.kilojoules.repositories.PortionRepository;
import com.pc.kilojoules.services.FoodCsvService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BootstrapData implements CommandLineRunner {

    private final FoodRepository foodRepository;
    private final FoodCsvService foodCsvService;
    private final PortionRepository portionRepository;
    private final MealRepository mealRepository;
    private final MealFoodRepository mealFoodRepository;

    public BootstrapData(FoodRepository foodRepository, FoodCsvService foodCsvService, PortionRepository portionRepository, MealRepository mealRepository, MealFoodRepository mealFoodRepository) {
        this.foodRepository = foodRepository;
        this.foodCsvService = foodCsvService;
        this.portionRepository = portionRepository;
        this.mealRepository = mealRepository;
        this.mealFoodRepository = mealFoodRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadCsvData();
        populatePortions();
        populateIndividualPortions();
        populateIndividualMeals();

    }

    private void loadCsvData() throws FileNotFoundException {
        if (foodRepository.count() < 10){
            File file = ResourceUtils.getFile("classpath:csvdata/potraviny.csv");

            List<FoodCSVRecord> recs = foodCsvService.convertCSV(file);

            recs.forEach(foodCSVRecord -> {

                foodRepository.save(Food.builder()
                                .name(foodCSVRecord.getName())
                                .amount(foodCSVRecord.getAmount())
                                .kiloJoules(foodCSVRecord.getKiloJoules())
                                .proteins(foodCSVRecord.getProteins())
                                .carbohydrates(foodCSVRecord.getCarbohydrates())
                                .fiber(foodCSVRecord.getFiber())
                                .sugar(foodCSVRecord.getSugar())
                                .fat(foodCSVRecord.getFat())
                                .safa(foodCSVRecord.getSafa())
                                .tfa(foodCSVRecord.getTfa())
                                .cholesterol(foodCSVRecord.getCholesterol())
                                .sodium(foodCSVRecord.getSodium())
                                .calcium(foodCSVRecord.getCalcium())
                                .phe(foodCSVRecord.getPhe())
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
                .food(foodRepository.findById(12L).orElseThrow())
                .quantity(BigDecimal.valueOf(70))
                .build();
        mealFoodRepository.save(mealFoodCibule);

        Set<MealFood> vejceCibule = new HashSet<>();
        vejceCibule.add(mealFoodVejce);
        vejceCibule.add(mealFoodCibule);

        vejceSCibuli.setMealFoods(vejceCibule);
        mealRepository.save(vejceSCibuli);
    }
}
